/*
 * MessageUtils.java
 *
 * Created on 2018-09-16, 16:09
 *
 * Copyright 2018 Marc Nuri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package edu.agiledev.agilemail.utils;


import com.sun.mail.imap.IMAPFolder;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.message.AMessage;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.util.HtmlUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;


/**
 * 邮件帮助类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
public class MessageUtil {

    public static final String MULTIPART_MIME_TYPE = "multipart/";

    private MessageUtil() {
    }

    /**
     * 获取给定 {@link Message}数组的信封及轻量级信息。
     *
     * @param folder   邮件所位于的文件夹
     * @param messages 需要获取信息的邮件数组
     */
    public static void envelopeFetch(@NonNull Folder folder, @NonNull Message[] messages)
            throws MessagingException {

        if (messages.length != 0) {
            final FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(UIDFolder.FetchProfileItem.UID);
            fp.add(IMAPFolder.FetchProfileItem.HEADERS);
            fp.add(FetchProfile.Item.FLAGS);
            fp.add(FetchProfile.Item.SIZE);
            folder.fetch(messages, fp);
        }
    }

    /**
     * 解析指定{@link Message.RecipientType}类型的收件人的{@link Address}列表
     */
    public static Address[] getRecipientAddresses(AMessage message, Message.RecipientType type) {
        if (message.getRecipients() == null || message.getRecipients().isEmpty()) {
            return new Address[0];
        }
        return message.getRecipients().stream()
                .filter(r -> type.toString().equals(r.getType()))
                .map(r -> {
                    try {
                        return new InternetAddress(r.getAddress());
                    } catch (AddressException e) {
                        throw new BaseException(ReturnCode.ADDRESS_ERROR, String.format("解析地址%s时发生错误", r.getAddress()), e);
                    }
                })
                .toArray(InternetAddress[]::new);
    }

    /**
     * 根据 {@link Multipart}容器抽取邮件内容
     * TODO: Check this is the right approach
     */
    public static String extractContent(@NonNull Multipart multipart) throws MessagingException, IOException {
        String res = "";
        for (int it = 0; it < multipart.getCount(); it++) {
            final BodyPart part = multipart.getBodyPart(it);
            if ((res == null || res.isEmpty())
                    && part.getContentType().toLowerCase().startsWith(MediaType.TEXT_PLAIN_VALUE)) {
                res = String.format("<pre>%s</pre>", HtmlUtils.htmlEscape(part.getContent().toString()));
            }
            if (part.getContentType().toLowerCase().startsWith(MediaType.TEXT_HTML_VALUE)) {
                res = part.getContent().toString();
            }
            if (part.getContentType().toLowerCase().startsWith(MULTIPART_MIME_TYPE)) {
                res = extractContent((Multipart) part.getContent());
            }
        }
        return res;
    }


    public static BodyPart extractBodyPart(Multipart mp, String id, boolean isContentId)
            throws MessagingException, IOException {

        return isContentId ?
                extractEmbeddedImageBodyPart(mp, id) :
                extractAttachmentBodyPart(mp, id);
    }


    /**
     * 将图片cid url(<code>&lt;img src='cid:1234' /&gt;</code>)替换为Base64数据urls
     */
    public static String replaceEmbeddedImage(@Nullable String content, @NonNull MimeBodyPart imageBodyPart)
            throws MessagingException, IOException {

        final String cid = imageBodyPart.getContentID().replaceAll("[<>]", "");
        if (content != null && content.contains(cid)) {
            String contentType = imageBodyPart.getContentType();
            if (contentType.contains(";")) {
                contentType = contentType.substring(0, contentType.indexOf(';'));
            }
            final String base64 = Base64.encodeBase64String(IOUtils.toByteArray(imageBodyPart.getInputStream()))
                    .replace("\r", "").replace("\n", "");
            return content.replace("cid:" + cid,
                    String.format("data:%s;%s,%s",
                            contentType,
                            imageBodyPart.getEncoding(),
                            base64));
        }
        return content;
    }

    /**
     * 从{@link Multipart}中抽取contentId对应的image的{@link BodyPart}
     *
     * @param multipart BodyPart所在
     * @param contentId BodyPart对应的image的id
     * @return contentId对应的image的BodyPart，如果没找到则返回null
     * @throws MessagingException for any IMAP exception
     * @throws IOException        for IO problems when reading the content
     */
    private static BodyPart extractEmbeddedImageBodyPart(Multipart multipart, String contentId)
            throws MessagingException, IOException {

        for (int it = 0; it < multipart.getCount(); it++) {
            final BodyPart bp = multipart.getBodyPart(it);
            if (bp.getContentType().toLowerCase().startsWith(MULTIPART_MIME_TYPE)) {
                final BodyPart nestedBodyPart = extractEmbeddedImageBodyPart((Multipart) bp.getContent(), contentId);
                if (nestedBodyPart != null) {
                    return nestedBodyPart;
                }
            }
            if (bp.getContentType().toLowerCase().startsWith("image/")
                    && bp instanceof MimeBodyPart
                    && contentId.equals(((MimeBodyPart) bp).getContentID())) {
                return bp;
            }
        }
        return null;
    }

    /**
     * 从{@link Multipart}中抽取aid对应的attachment的{@link BodyPart}
     *
     * @param multipart BodyPart所在
     * @param aid       attachment对应的BodyPart的id
     * @return aid对应的附件的BodyPart，如果没找到则返回null
     * @throws MessagingException IMAP异常
     * @throws IOException        读取content时发生IO异常时抛出
     */
    private static BodyPart extractAttachmentBodyPart(Multipart multipart, String aid)
            throws MessagingException, IOException {

        for (int it = 0; it < multipart.getCount(); it++) {
            final BodyPart bp = multipart.getBodyPart(it);
            if (bp.getDisposition() != null && Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition())) {
                // Regular file
                if (aid.equals(MimeUtility.decodeText(bp.getFileName()))) {
                    return bp;
                }
                // Embedded message
                if (bp.getContentType().toLowerCase().startsWith("message/")
                        && bp.getContent() instanceof MimeMessage
                        && ((MimeMessage) bp.getContent()).getSubject().equals(aid)) {
                    return bp;
                }
            }
        }
        return null;
    }


}

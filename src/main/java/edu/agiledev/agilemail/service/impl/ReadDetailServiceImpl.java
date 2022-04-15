package edu.agiledev.agilemail.service.impl;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import edu.agiledev.agilemail.pojo.message.AMessage;
import edu.agiledev.agilemail.pojo.message.Attachment;
import edu.agiledev.agilemail.service.ReadDetailService;
import edu.agiledev.agilemail.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.agiledev.agilemail.utils.MessageUtil.MULTIPART_MIME_TYPE;

/**
 * 读取邮件详情服务实现类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
@Service
@Slf4j
public class ReadDetailServiceImpl implements ReadDetailService {

    private final Long embeddedImageSize;

    @Autowired
    public ReadDetailServiceImpl(@Value("${config.embedded.imageSize}") Long embeddedImageSize) {
        this.embeddedImageSize = embeddedImageSize;
    }

    @Override
    public AMessage readMessageDetail(IMAPFolder folder, IMAPMessage imapMessage) throws MessagingException, IOException {
        if (!folder.isOpen()) {
            return null;
        }
        AMessage res = AMessage.from(folder, imapMessage);
        readContentIntoMessage(imapMessage, res);
        return res;
    }


    /**
     * 读取邮件内容
     */
    private void readContentIntoMessage(IMAPMessage imapMessage, AMessage message) throws MessagingException, IOException {
        final Object content = imapMessage.getContent();
        if (content instanceof Multipart) {
            message.setContent(MessageUtil.extractContent((Multipart) content));
//            message.setAttachments(addLinks(AFolder.toBase64Id(folderId), message, extractAttachments(message, (Multipart) content, null)));
            message.setAttachments(extractAttachments(message, (Multipart) content, null));
        } else if (content instanceof MimeMessage
                && ((MimeMessage) content).getContentType().toLowerCase().contains("html")) {
            message.setContent(content.toString());
        } else if (imapMessage.getContentType().indexOf(MediaType.TEXT_HTML_VALUE) == 0) {
            message.setContent(content.toString());
        } else {
            //Preserve formatting
            message.setContent(content.toString()
                    .replace("\r\n", "<br />")
                    .replaceAll("[\\r\\n]", "<br />"));
        }
    }

    /**
     * 返回装有{@link Attachment}的list对象并将{@link Message#getContent()}中的足够小的内嵌图片替换以减小飞来可能的开销。
     */
    private List<Attachment> extractAttachments(
            @NonNull AMessage finalMessage, @NonNull Multipart mp, @Nullable List<Attachment> attachments)
            throws MessagingException, IOException {

        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        for (int it = 0; it < mp.getCount(); it++) {
            final BodyPart part = mp.getBodyPart(it);
            // Multipart message with embedded parts
            if (part.getContentType().toLowerCase().startsWith(MULTIPART_MIME_TYPE)) {
                extractAttachments(finalMessage, (Multipart) part.getContent(), attachments);
            }
            // Image attachments
            else if (part.getContentType().toLowerCase().startsWith("image/")
                    && part instanceof MimeBodyPart
                    && ((MimeBodyPart) part).getContentID() != null) {
                // If image is "not too big" embed as base64 data uri - successive IMAP connections will be more expensive
                if (part.getSize() <= embeddedImageSize) {
                    finalMessage.setContent(MessageUtil.replaceEmbeddedImage(finalMessage.getContent(), (MimeBodyPart) part));
                } else {
                    attachments.add(new Attachment(
                            ((MimeBodyPart) part).getContentID(), part.getFileName(), part.getContentType(), part.getSize()));
                }
            }
            // Embedded messages
            else if (part.getContentType().toLowerCase().startsWith("message/")) {
                final Object nestedMessage = part.getContent();
                if (nestedMessage instanceof MimeMessage) {
                    attachments.add(new Attachment(null, ((MimeMessage) nestedMessage).getSubject(),
                            part.getContentType(), ((MimeMessage) nestedMessage).getSize()));
                }
            }
            // Regular files
            else if (part.getDisposition() != null && part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
                attachments.add(new Attachment(
                        null, MimeUtility.decodeText(part.getFileName()), part.getContentType(), part.getSize()));
            }
        }
        return attachments;
    }
}

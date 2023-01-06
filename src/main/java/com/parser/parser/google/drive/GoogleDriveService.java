package com.parser.parser.google.drive;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.parser.parser.google.GoogleProvider;
import com.parser.parser.notifications.EmailService;
import com.parser.parser.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Log4j2
@Component
@RequiredArgsConstructor
public class GoogleDriveService {
    private final GoogleProvider googleProvider;
    private final EmailService emailService;
    @Value("${notification.email.admin}")
    private String adminEmail;

    public String getGoogleFileUrl(File file) {
        return Constants.GOOGLE_FILE_URL + file.getId();
    }

    public File sendFile(String userEmail) {
        if(googleProvider.getDriveService() != null) {
            Drive driveService = googleProvider.getDriveService();
            File googleFile = null;

            try {
                File fileMetadata = new File();
                String fileName = LocalDateTime.now() + " result.xlsx";
                fileMetadata.setName(fileName);
                java.io.File filePath = new java.io.File(Constants.LOCAL_FILE_PATH);
                FileContent mediaContent = new FileContent(Constants.XLSX_MIME_TYPE, filePath);
                googleFile = driveService.files().create(fileMetadata, mediaContent)
                        .setFields("id")
                        .setIncludePermissionsForView("published")
                        .execute();
                googleFile.setCopyRequiresWriterPermission(true);
                setViewPermissionsForUser(driveService, googleFile.getId(), userEmail);
            } catch (IOException e) {
                log.warn("File wasn't sent");
                emailService.sendSimpleEmail(adminEmail, "Google token error!",
                        "Please, check google drive token");
            }
            log.info("file has been sent");
            return googleFile;
        }
        return null;
    }

    public void setViewPermissionsForUser(Drive driveService, String fileId, String userEmail) {
        JsonBatchCallback<Permission> callback = new JsonBatchCallback<>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders) {
                System.err.println(e.getMessage());
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders) {
                System.out.println("Permission ID: " + permission.getId());
            }
        };

        BatchRequest batch = driveService.batch();
        Permission userPermission = new Permission()
                .setType("user")
                .setRole("writer");
        userPermission.setEmailAddress(userEmail);
        try {
            driveService.permissions().create(fileId, userPermission)
                    .setFields("id")
                    .queue(batch, callback);
            batch.execute();
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to modify permission: " + e.getDetails());
            log.error("Can't get permissions for file with id: " + fileId, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

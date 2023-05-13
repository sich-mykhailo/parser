package com.parser.parser.google.drive;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.parser.parser.google.GoogleProvider;
import com.parser.parser.notifications.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.parser.parser.utils.Constants.*;

@Log4j2
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GoogleDriveService {
    GoogleProvider googleProvider;
    EmailService emailService;
    @Value("${notification.email.admin}")
    @NonFinal String adminEmail;

    public String getGoogleFileUrl(File file) {
        return GOOGLE_FILE_URL + file.getId();
    }

    public File sendFile(String userEmail, ByteArrayInputStream file) {
        if (googleProvider.getDriveService() != null) {
            Drive driveService = googleProvider.getDriveService();
            File googleFile = null;

            try {
                File fileMetadata = new File();
                String fileName = LocalDateTime.now() + " result.xlsx";
                fileMetadata.setName(fileName);
                ByteArrayContent mediaContent = new ByteArrayContent("application/octet-stream", file.readAllBytes());
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
        return new File();
    }

    public void setViewPermissionsForUser(Drive driveService, String fileId, String userEmail) {
        JsonBatchCallback<Permission> callback = new JsonBatchCallback<>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders) {
              log.warn(e.getMessage());
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders) {
               log.info("Permission ID: " + permission.getId());
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
        } catch (IOException e) {
            log.error("Can't get permissions for file with id: " + fileId, e);
        }
    }
}

/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.seagrid.desktop;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.seagrid.desktop.ui.commons.SEAGridDialogHelper;
import org.seagrid.desktop.ui.home.HomeWindow;
import org.seagrid.desktop.util.SEAGridContext;
import org.seagrid.desktop.util.messaging.SEAGridEvent;
import org.seagrid.desktop.util.messaging.SEAGridEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SEAGridDesktop extends Application{
    private static Logger logger;

    public SEAGridDesktop(){
        SEAGridEventBus.getInstance().register(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initApplicationDirs();
        File dataDir = new File(applicationDataDir());
        if(dataDir.exists()) {
//            LoginWindow loginWindow =  new LoginWindow();
//            loginWindow.displayLoginAndWait();
            SEAGridContext.getInstance().setAuthenticated(true);

            SEAGridContext.getInstance().setOAuthToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJtckJrdU55WDlQbXJqYmR4aUNQXzA0T01ZaHUwdXEtT1JGMmhnanBPNEdZIn0.eyJqdGkiOiI5YWM2M2Q5Mi1mNDM5LTQ3YTgtYjA0Ny0wYjdkOTAxYzMyYTEiLCJleHAiOjE1Mzg5NTUxMDAsIm5iZiI6MCwiaWF0IjoxNTM4NTIzMTAwLCJpc3MiOiJodHRwczovLzE5Mi4xNjguNTcuMy9hdXRoL3JlYWxtcy9kZWZhdWx0IiwiYXVkIjoicGdhIiwic3ViIjoiZTRkODYwM2MtNzAxZi00NzlhLWFhOTEtZWJkZWQwMjc5MzQyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicGdhIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiMmZlODNmOGUtZDNlNi00Y2VhLWJhYjYtMjM4NzM5MmRlODA0IiwiYWNyIjoiMSIsImNsaWVudF9zZXNzaW9uIjoiNWY4N2I1NWYtYTdjMi00Y2FmLWFlNjctZWQ3N2RjMGIzZTA2IiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly8xOTIuMTY4LjU3LjMiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwidmlldy1hdXRob3JpemF0aW9uIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiS2FyYW4gS290YWJhZ2kiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJkZWZhdWx0LWFkbWluIiwiZ2l2ZW5fbmFtZSI6IkthcmFuIiwiZmFtaWx5X25hbWUiOiJLb3RhYmFnaSIsImVtYWlsIjoia2FyYW5rb3R6QGdtYWlsLmNvbSJ9.AEZ38yvmsFap9uEHFxbCXGQL8QDf3Q353dxORJNosMnbnGyGfF8wn4M3m3VNa_zBSAlz5aFqiRUXAU4vFTHindXlgpfcprnEZ6waJj7D1oeGS9f3Qyh3w8oCabsw4p7NTkiU5FWDJUnFUuTTy7E3yjQo4ngdX-TvyU50EKpNnN6jawSDUDQE-sz29WsZIVBskqLCt37QvNG8grncNtjikVsMoIbKmVOj19p4lxZvi9pM9w3jkVnbM80g1Kf5sx5vX3h_7ieqxVHDw8bjFuy7OIUdfrkABbLZboF1drklIyhkpDetDpM0dlhRtZkjFCJ3IPy7Flnd1jsbpsC721jDfg");
            SEAGridContext.getInstance().setRefreshToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJtckJrdU55WDlQbXJqYmR4aUNQXzA0T01ZaHUwdXEtT1JGMmhnanBPNEdZIn0.eyJqdGkiOiJiOGM2NTY3YS0xYjYzLTQxODEtYjBjNi03NjdiNTA2MmVlOWUiLCJleHAiOjE1Mzg1MjQ5MDAsIm5iZiI6MCwiaWF0IjoxNTM4NTIzMTAwLCJpc3MiOiJodHRwczovLzE5Mi4xNjguNTcuMy9hdXRoL3JlYWxtcy9kZWZhdWx0IiwiYXVkIjoicGdhIiwic3ViIjoiZTRkODYwM2MtNzAxZi00NzlhLWFhOTEtZWJkZWQwMjc5MzQyIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6InBnYSIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjJmZTgzZjhlLWQzZTYtNGNlYS1iYWI2LTIzODczOTJkZTgwNCIsImNsaWVudF9zZXNzaW9uIjoiNWY4N2I1NWYtYTdjMi00Y2FmLWFlNjctZWQ3N2RjMGIzZTA2IiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwidmlldy1hdXRob3JpemF0aW9uIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19fQ.MaxZ-JfGJsLrtFVeYpRkuJU2PfvdzEZUOZ29Pkg5LjXTzXnGAsnNdj4LGRT4ivCdT4BYARsldtlfKFuQRRC8884z243inkTz68ClAOnyTQWzzsj3B3hVSDQ4967kOk438Iyi2ZKE7w_LlMWnAYrv5AugGWXysrCXoo_B2aWeJe2zcYH5bU5S0a6hyltqULfqkzK30hSWdmFPLpI7tKa36Zz0KTyMoHHMIm7yUqFgD9CaGGM-MD7rNmpjJFr11U87H5yPHZLGRStY8bwZOxXlxvA0JUF5K87nkYlUF65nsEceYJTgWUQRBX4ClmO_FSGPk6K3MmsOTp2q5sA1MaRwcQ");
            SEAGridContext.getInstance().setTokenExpiaryTime(1538954800);
            SEAGridContext.getInstance().setUserName("default-admin");
           /* SEAGridContext.getInstance().setOAuthToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJtckJrdU55WDlQbXJqYmR4aUNQXzA0T01ZaHUwdXEtT1JGMmhnanBPNEdZIn0.eyJqdGkiOiI5YWFmNjZmMC04NzAwLTRlNGYtODFiZC1mMjZmMjIwYTkxMmYiLCJleHAiOjE1Mzg0Mzk4NzYsIm5iZiI6MCwiaWF0IjoxNTM4MDA3ODc2LCJpc3MiOiJodHRwczovLzE5Mi4xNjguNTcuMy9hdXRoL3JlYWxtcy9kZWZhdWx0IiwiYXVkIjoicGdhIiwic3ViIjoiZTRkODYwM2MtNzAxZi00NzlhLWFhOTEtZWJkZWQwMjc5MzQyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicGdhIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiYTQ5NzNiY2QtN2UzMi00YzJhLWFmOWMtN2JmYThiODM1NWZhIiwiYWNyIjoiMSIsImNsaWVudF9zZXNzaW9uIjoiNDkzYTIyODktZWVjYS00MjE2LWE5YmItZWM1NmEzZDQzYWE4IiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly8xOTIuMTY4LjU3LjMiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwidmlldy1hdXRob3JpemF0aW9uIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiS2FyYW4gS290YWJhZ2kiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJkZWZhdWx0LWFkbWluIiwiZ2l2ZW5fbmFtZSI6IkthcmFuIiwiZmFtaWx5X25hbWUiOiJLb3RhYmFnaSIsImVtYWlsIjoia2FyYW5rb3R6QGdtYWlsLmNvbSJ9.Ie5IZ-X8VzJCBFZ5GBcOfoY8Eiy85A5-csUHOqZZETWUT0fW0x1Pl0TBe8xOI5vdpLi0YQrUcJ9IZAWafQvWGFvgN22UWZIkiFeZUiaa6tsIjB0pq8ZqBF5ztSeo3uPIOo9B3m_Zv1ZbzzZyFOsihP92B6aqEbkDpulW8wPkZPDml6TAk0sMnDtRJ67UqnoDGfLTphoj_bEzj29kVNytf9iYAQQgGVp_rvYNx5SI3A1zaAav-ANDuiuP7aKNZBXdiYZTBOGGMRtwEr7ISqN5u2Z2N5WFVH_K_y_u5l-lhZofH3LntYTpDUp0QDST2QEAZ3FOZFN58gShgl-W7SlS5Q");
            SEAGridContext.getInstance().setRefreshToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJtckJrdU55WDlQbXJqYmR4aUNQXzA0T01ZaHUwdXEtT1JGMmhnanBPNEdZIn0.eyJqdGkiOiIzZjE5ZmYxNS0wMjU1LTQzODctYTdhYS1mYTViY2Q0NGYyNmMiLCJleHAiOjE1MzgwMDk2NzYsIm5iZiI6MCwiaWF0IjoxNTM4MDA3ODc2LCJpc3MiOiJodHRwczovLzE5Mi4xNjguNTcuMy9hdXRoL3JlYWxtcy9kZWZhdWx0IiwiYXVkIjoicGdhIiwic3ViIjoiZTRkODYwM2MtNzAxZi00NzlhLWFhOTEtZWJkZWQwMjc5MzQyIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6InBnYSIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6ImE0OTczYmNkLTdlMzItNGMyYS1hZjljLTdiZmE4YjgzNTVmYSIsImNsaWVudF9zZXNzaW9uIjoiNDkzYTIyODktZWVjYS00MjE2LWE5YmItZWM1NmEzZDQzYWE4IiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwidmlldy1hdXRob3JpemF0aW9uIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19fQ.lx3WaZT_OVYSUokqtENc96MOsQQaH39PfV_hfyBZdwF7ZE0VXURBqOxfnK_yWgZnaAm2KaceO3FEzoC8KXG03uNbldAwx9qEk9wgMTXlWtmrp9sUnU_GCbrNzjPlVODnaRM9A2KAtOO45K7DSRAFROPxIWat0O7ty_YxysMot4wCBlaPybAHOnPdgazbDtmkmjsYzpvFZ9wsdOcgjaHZqBrJJNzefap91ggwO35Q7Z4RfUK0MmBHWEc6KTFg5vEwWL7CcsF3dlPVonwEsl7sMdYSGBRoVv0qIQDx84EY0TfSkna4bTDg6K_fdNV_et8wssNcd1mACColyKuIVRuj_g");
            SEAGridContext.getInstance().setTokenExpiaryTime(1538439576);
            SEAGridContext.getInstance().setUserName("default-admin");*/
            boolean isAuthenticated = SEAGridContext.getInstance().getAuthenticated();
            if (isAuthenticated) {
                HomeWindow homeWindow = new HomeWindow();
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                primaryStage.setX(bounds.getMinX());
                primaryStage.setY(bounds.getMinY());
                primaryStage.setWidth(bounds.getWidth());
                primaryStage.setHeight(bounds.getHeight());
                homeWindow.start(primaryStage);
                primaryStage.setOnCloseRequest(t -> {
                    Platform.exit();
                    System.exit(0);
                });
            }
        }else{
            SEAGridDialogHelper.showExceptionDialogAndWait(new Exception("Application Data Dir Does Not Exists"),
                    "Application Data Dir Does Not Exists", null, "Application Data Dir Does Not Exists");
            System.exit(0);
        }
    }

    @Subscribe
    public void handleSEAGridEvents(SEAGridEvent event){
        if(event.getEventType().equals(SEAGridEvent.SEAGridEventType.LOGOUT)){
            try {
                start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        initApplicationDirs();
        launch(args);
    }

    public static void initApplicationDirs() throws IOException {
        createTrustStoreFileIfNotExists();
        File appDataRoot = new File(applicationDataDir());
        if(!appDataRoot.exists()){
            appDataRoot.mkdirs();
        }
        if(!appDataRoot.canWrite()){
            SEAGridDialogHelper.showExceptionDialogAndWait(new Exception("Cannot Write to Application Data Dir"),
                    "Cannot Write to Application Data Dir", null, "Cannot Write to Application Data Dir " + applicationDataDir());
        }

        //Legacy editors use stdout and stderr instead of loggers. This is a workaround to append them to a file
        System.setProperty("app.data.dir", applicationDataDir() + "logs");
        logger = LoggerFactory.getLogger(SEAGridDesktop.class);
        File logParent = new File(applicationDataDir() + "logs");
        if(!logParent.exists())
            logParent.mkdirs();
        PrintStream outPs = new PrintStream(applicationDataDir() + "logs/seagrid.std.out");
        PrintStream errPs = new PrintStream(applicationDataDir() + "logs/seagrid.std.err");
        System.setOut(outPs);
        System.setErr(errPs);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                outPs.close();
                errPs.close();
                SEAGridContext.getInstance().saveUserPrefs();
            }
        });

        extractLegacyEditorResources();
    }

    public static void createTrustStoreFileIfNotExists() throws IOException {
        File parentDir = new File(applicationDataDir());
        if(!parentDir.exists())
            parentDir.mkdirs();
        File targetFile = new File(applicationDataDir() + "client_truststore.jks");
        if(!targetFile.exists()) {
            InputStream initialStream = SEAGridContext.class.getResourceAsStream("/client_truststore.jks");
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        }
    }

    public static void extractLegacyEditorResources() {
        try {
            String destParent = applicationDataDir() + ".ApplicationData" + File.separator;
            File appHome = new File(destParent);
            if(!appHome.exists()){
                if(!appHome.mkdirs()){
                    SEAGridDialogHelper.showExceptionDialogAndWait(new Exception("Cannot Create Application Data Dir"),
                            "Cannot Create Application Data Dir", null, "Cannot Create Application Data Dir");
                }
            }
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(SEAGridDesktop.class.getClassLoader().getResourceAsStream("legacy.editors.zip"));

            zipentry = zipinputstream.getNextEntry();
            if(zipentry == null){
                SEAGridDialogHelper.showExceptionDialogAndWait(new Exception("Cannot Read Application Resources"),
                        "Cannot Read Application Resources", null, "Cannot Read Application Resources");
            }else {
                while (zipentry != null) {
                    //for each entry to be extracted
                    String entryName = destParent + zipentry.getName();
                    entryName = entryName.replace('/', File.separatorChar);
                    entryName = entryName.replace('\\', File.separatorChar);
                    logger.info("entryname " + entryName);
                    int n;
                    FileOutputStream fileoutputstream;
                    File newFile = new File(entryName);
                    if (zipentry.isDirectory()) {
                        if (!newFile.mkdirs()) {
                            break;
                        }
                        zipentry = zipinputstream.getNextEntry();
                        continue;
                    }
                    fileoutputstream = new FileOutputStream(entryName);
                    while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                        fileoutputstream.write(buf, 0, n);
                    }
                    fileoutputstream.close();
                    zipinputstream.closeEntry();
                    zipentry = zipinputstream.getNextEntry();
                }
                zipinputstream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    private static String applicationDataDir()
    {
        return System.getProperty("user.home") + File.separator + "SEAGrid" + File.separator;
    }
}
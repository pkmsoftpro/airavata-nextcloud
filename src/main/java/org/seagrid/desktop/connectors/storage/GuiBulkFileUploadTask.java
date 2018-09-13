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
package org.seagrid.desktop.connectors.storage;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.seagrid.desktop.util.SEAGridContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


public class GuiBulkFileUploadTask extends GuiFileTask {
    private final static Logger logger = LoggerFactory.getLogger(GuiBulkFileUploadTask.class);

    private Map<String, File> uploadFiles;

    public GuiBulkFileUploadTask(Map<String, File> uploadFiles) throws JSchException {
        super();
        this.uploadFiles = uploadFiles;
    }

    @Override
    protected Boolean call() throws Exception {
        //return uploadFiles(this.uploadFiles);
        //next.UploadFile(uploadFiles.get())
        return uploadtonextcloud();
    }

    public boolean uploadFiles(Map<String, File> uploadFiles) throws IOException, SftpException {
        int numberOfFiles = uploadFiles.size();
        int index = 1;
        for(String remoteFilePath : uploadFiles.keySet()) {
            remoteFilePath = remoteFilePath.replace("\\","/");
                //System.out.println(entry.getKey() + ":" + entry.getValue().toString());
                //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, remoteFilePath, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                //alert.show();

            //System.out.println(remoteFilePath);
            //My code
            /*
            String str = "Hello";
            String fileName="create.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(""+remoteFilePath+"\n");
            */
            createRemoteParentDirsIfNotExists(Paths.get(remoteFilePath).getParent().toString());

            OutputStream remoteOutputStream = new BufferedOutputStream(channelSftp.put(remoteFilePath));
            File localFile = uploadFiles.get(remoteFilePath);
            String localpath = localFile.getPath();
            String remotepath = "Documents" + remoteFilePath;
            //writer.write(""+next.UploadFile("/Users/kkotabag/Desktop/test/Fileupload/third.txt","/Documents/third.txt")+"");
            InputStream localInputStream = new FileInputStream(localFile);
            //writer.close();
            long fileSize = localFile.length();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            int totalBytesRead = 0;
            double percentCompleted = 0;

            while ((bytesRead = localInputStream.read(buffer)) != -1) {
                remoteOutputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                percentCompleted = ((double)totalBytesRead) / fileSize * index / numberOfFiles;
                updateMessage("Uploaded " + totalBytesRead + " bytes");
                updateProgress(percentCompleted, 1);
            }

            remoteOutputStream.close();
            localInputStream.close();
            index++;
        }
        return true;
    }


    public boolean uploadtonextcloud() throws IOException {
        for(String remoteFilePath : uploadFiles.keySet()){
            remoteFilePath = remoteFilePath.replace("\\","/");
            //System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, remoteFilePath, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            //alert.show();
            //System.out.println(remoteFilePath);
            //My code
            String str = "Hello";
            String fileName="create.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(""+remoteFilePath+"\n");
            File localFile = uploadFiles.get(remoteFilePath);
            String localpath = localFile.getPath();
            writer.write(localpath);

            String remotepath = remoteFilePath;

            writer.write(""+next.UploadFile(localpath,remotepath)+"");
            //writer.write(SEAGridContext.getInstance().getOAuthToken());
            InputStream localInputStream = new FileInputStream(localFile);
            writer.close();
        }
        return true;
    }

    private void createRemoteParentDirsIfNotExists(String parentDirPath) throws SftpException {
        parentDirPath = parentDirPath.replace("\\","/");
        channelSftp.cd("/");
        String[] folders = parentDirPath.split( "/" );
        for ( String folder : folders ) {
            if ( folder.length() > 0 ) {
                try {
                    channelSftp.cd(folder);
                }
                catch ( SftpException e ) {
                    channelSftp.mkdir( folder );
                    channelSftp.chmod(Integer.parseInt("777",8), folder);
                    channelSftp.cd( folder );
                }
            }
        }
    }
}
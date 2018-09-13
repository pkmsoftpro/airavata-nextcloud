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

import com.github.sardine.DavResource;
import com.jcraft.jsch.*;
import javafx.scene.control.ProgressIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Vector;


public class GuiDirDownloadTask extends GuiFileTask {
    private final static Logger logger = LoggerFactory.getLogger(GuiDirDownloadTask.class);

    private String remoteDirPath, localDirPath;
    private double totalBytesRead = 0, totalSize = 0;

    public GuiDirDownloadTask(String remoteDirPath, String localDirPath) throws JSchException {
        super();
        this.remoteDirPath = remoteDirPath;
        this.localDirPath = localDirPath;
    }

    @Override
    protected Boolean call() throws Exception {
        String fileName="CalculateSizeDownloadFolderLog.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        String fileName2="CalculateSizeNextcloudDownloadFolderLog.txt";
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(fileName2));
        calculateNextcloudTotalSize(remoteDirPath, writer2);
        calculateTotalSize(remoteDirPath, writer);
        writer.close();
        writer2.close();
        return downloadDir(remoteDirPath, localDirPath);
        //return nextcloudDownloadDir(remoteDirPath, localDirPath);
    }

    public Boolean downloadDir(String remoteDirPath, String localDirPath) throws SftpException, IOException {

        String fileName="DirDownloadLog.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("\nRemotepath ---> "+remoteDirPath);
        writer.write("\nLocalpath--->"+localDirPath);
        writer.close();

        //Creating the local directory if the path doesn't exist
        File localDir = new File(localDirPath);
        if(!localDir.exists()){
            localDir.mkdirs();
        }

        Vector<ChannelSftp.LsEntry> lsEntries = channelSftp.ls(remoteDirPath);
        for(ChannelSftp.LsEntry lsEntry : lsEntries){
            if(lsEntry.getFilename().equals(".") || lsEntry.getFilename().equals("..")){
                continue;
            }
            if(lsEntry.getAttrs().isDir()){
                String tempLocalDir = localDirPath + File.separator + lsEntry.getFilename();
                String tempRemoteDir = remoteDirPath + "/" + lsEntry.getFilename();
                downloadDir(tempRemoteDir, tempLocalDir);
            }else{
                File localFile = new File(localDirPath + File.separator + lsEntry.getFilename());
                OutputStream localOutputStream = new FileOutputStream(localFile);
                BufferedInputStream remoteInputStream = new BufferedInputStream(channelSftp.get(remoteDirPath
                        + "/" + lsEntry.getFilename()));
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = remoteInputStream.read(buffer)) != -1) {
                    localOutputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    updateMessage("Downloaded " + totalBytesRead + " bytes");
                    updateProgress(totalBytesRead, totalSize);
                }
                remoteInputStream.close();
                localOutputStream.close();
            }
        }
        return true;
    }

    

    private void calculateTotalSize(String remoteDirPath, BufferedWriter writer) throws SftpException, IOException {
        Vector<ChannelSftp.LsEntry> lsEntries = channelSftp.ls(remoteDirPath);

        for(ChannelSftp.LsEntry lsEntry : lsEntries){
            if(lsEntry.getFilename().equals(".") || lsEntry.getFilename().equals("..")){
                continue;
            }
            if(lsEntry.getAttrs().isDir()){
                String tempRemoteDir = remoteDirPath + "/" + lsEntry.getFilename();
                calculateTotalSize(tempRemoteDir, writer);
            }else {
                totalSize += lsEntry.getAttrs().getSize();
                writer.write("\n Total Size"+totalSize);
            }
        }
    }

    private void calculateNextcloudTotalSize(String remoteDirPath, BufferedWriter writer2) throws SftpException, IOException {
        //Vector<ChannelSftp.LsEntry> lsEntries = channelSftp.ls(remoteDirPath);
        List<DavResource> davResource = next.listDirectories(remoteDirPath);
        // for(ChannelSftp.LsEntry lsEntry : lsEntries){
        int count = 0;
        for (DavResource res : davResource) {
            if(count != 0) {
                if (res.getName().equals(".") || res.getName().equals("..")) {
                    continue;
                }
                if (res.isDirectory()) {
                    String tempRemoteDir = remoteDirPath + "/" + res.getName();
                    calculateNextcloudTotalSize(tempRemoteDir, writer2);
                } else {
                    totalSize += res.getContentLength().intValue();
                    writer2.write("\n TotalSize" + totalSize);
                }
            }
            count++;
        }
    }


    public boolean nextcloudDownloadDir(String remoteDirPath, String localDirPath) throws IOException {

        String downlfile="DownloadDirNextcloudEntry.txt";
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(downlfile));
        if(next.downloadFolder(remoteDirPath, localDirPath, writer1)) {
            writer1.close();
            return true;
        }
        return false;
    }

}
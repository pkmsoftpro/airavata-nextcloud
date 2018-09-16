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
package org.seagrid.desktop.ui.prestageupload.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.seagrid.desktop.connectors.NextcloudStorage.NextcloudFileUploadTask;
import org.seagrid.desktop.ui.commons.SEAGridDialogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PrestageUploadController {
    private final static Logger logger = LoggerFactory.getLogger(PrestageUploadController.class);

    @FXML
    private Button pickFile;

    @FXML
    private Button saveButton;

    private FileChooser fileChooser;

    @FXML
    public GridPane preuploadGridPane;

    @FXML
    public Label chosenFile;

    Map<String,File> uploadFiles = new HashMap<>();

    @FXML
    public void initialize() {

        pickFile.setOnAction(event -> {
            try {
                fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    String fileAsString = file.getName();
                    String remotePath = "/Documents/InputFiles/" + fileAsString;
                    String filePath = file.toString();
                    chosenFile.setText("File:  " + fileAsString);
                    uploadFiles.put(remotePath, file);
                } else {
                    chosenFile.setText(null);
                }

            } catch (Exception ex) {
                SEAGridDialogHelper.showExceptionDialog(ex, "Caught Exception", null, "Unable to select file");
            }
        });

        saveButton.setOnAction(event -> {
            try {
                if (uploadFiles.size() > 0)
                {
                    Service<Boolean> fileUploadService = getPreFileUploadService(uploadFiles);
                    fileUploadService.start();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("File Upload Status");
                    alert.setContentText("File Upload Completed");
                    alert.showAndWait();
                }
            } catch (Exception e) {

            }
        });

    }

    public Service<Boolean> getPreFileUploadService(Map<String, File> uploadFiles) {
        Service<Boolean> service = new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                try {
                    return new NextcloudFileUploadTask(uploadFiles);
                } catch (Exception e) {
                    e.printStackTrace();
                    SEAGridDialogHelper.showExceptionDialogAndWait(e, "Exception Dialog", chosenFile.getScene().getWindow(),
                            "Unable To Connect To File Server !");
                }
                return null;
            }
        };
        SEAGridDialogHelper.showProgressDialog(service,"Progress Dialog",chosenFile.getScene().getWindow(),
                "Uploading Experiment Input Files");
        service.setOnFailed((WorkerStateEvent t) -> {
            SEAGridDialogHelper.showExceptionDialogAndWait(service.getException(), "Exception Dialog",
                    chosenFile.getScene().getWindow(), "File Upload Failed");
        });
        return service;
    }

}
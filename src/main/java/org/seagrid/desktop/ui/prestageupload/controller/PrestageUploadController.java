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

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
    public void initialize() {

        pickFile.setOnAction(event -> {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File selectedFiles = fileChooser.showOpenDialog(null);
        });
    }


}
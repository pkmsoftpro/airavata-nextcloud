package org.seagrid.desktop.connectors.NextcloudStorage;

import org.seagrid.desktop.connectors.NextcloudStorage.Exception.NextcloudApiException;
import org.seagrid.desktop.util.SEAGridContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NextcloudFolderUploadTask extends NextcloudFileTask {

    private String remoterootpath;
    private String remoteDirPath, localDirPath;

    public NextcloudFolderUploadTask(String remoteDirPath, String localDirPath) throws IOException {
        super();
        this.remoteDirPath = remoteDirPath;
        this.localDirPath = localDirPath;
        remoterootpath = (isusehttps ? "https" : "http") + "://" + servername + "/" + basepath + "/" + SEAGridContext.getInstance().getUserName();
    }

    @Override
    protected Boolean call() throws Exception {
        return uploadFolder(localDirPath, remoteDirPath);
    }

    /**
     * Upload the folder at the specified path, if the folder doesn't exist a new folder with
     * the specific foldername is created
     *
     * @param localpath
     * @param remotepath
     * @throws IOException
     */
    public Boolean uploadFolder(String localpath, String remotepath) throws IOException {
        String path;
        path = remoterootpath + remotepath;
        String ip = localpath;
        File f = new File(ip);
        try {
            if (f.exists() && f.isDirectory()) {
                //Extract the Foldername from the path
                String[] segments = ip.split("/");
                String foldername = segments[segments.length - 1];
                String folderemotepath = remotepath + "/" + foldername;
                String checkpath = remoterootpath + "/" + folderemotepath;

                //if the folder doesn't exist in the remote server then create the folder
                if (!sardine.exists(checkpath)) {
                    createFolder(folderemotepath);
                }

                File[] listfil = f.listFiles();
                if (listfil != null) {
                    for (File child : listfil) {
                        if(child.isDirectory()) {
                            String childfoldername = child.getName();
                            String newremotepath = folderemotepath;
                            String newlocalpath = localpath + "/" + childfoldername;
                            uploadFolder(newlocalpath, newremotepath);
                        } else {
                            String filename = child.getName();
                            String newpath = path + "/" + foldername + "/" + filename;
                            InputStream input = new FileInputStream(child.getAbsolutePath());
                            sardine.put(newpath, input);
                            input.close();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new NextcloudApiException(e);
        }
        return true;
    }
}

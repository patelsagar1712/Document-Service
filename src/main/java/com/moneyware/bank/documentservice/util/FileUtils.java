package com.moneyware.bank.documentservice.util;

import com.moneyware.bank.documentservice.entity.FilesEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class FileUtils {
    static ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    public static File generateIndexFile(FilesEntity fileDB) throws IOException {


        File file = new File(classLoader.getResource(".").getFile() + "/" + "DS" + Calendar.getInstance().getTimeInMillis() + ".txt");
        if (file.createNewFile()) {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write("COMMENT: ONDEMAND GENERIC INDEX FILE GENERATED");
            bw.newLine();
            bw.write("GROUP FILED NAME: TIMESTAMP");
            bw.newLine();

            bw.write("GROUP FILED VALUE:" + fileDB.getTimeStamp());

            bw.newLine();
            bw.write("GROUP FILED NAME: CUSTOMER_ID");
            bw.newLine();
            bw.write("GROUP FILED VALUE:" + fileDB.getCustomerId());

            bw.newLine();
            bw.write("GROUP FILED NAME: DOCUMENT_TYPE");
            bw.newLine();
            bw.write("GROUP FILED VALUE:" + fileDB.getDocumentType());

            bw.newLine();
            bw.write("GROUP FILE NAME : " + fileDB.getFileName());
            bw.close();

        }
        return file;
    }
}

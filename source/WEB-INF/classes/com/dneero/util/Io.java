package com.dneero.util;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 14, 2006
 * Time: 3:20:54 PM
 */
public class Io {


    /**
         * Reads the contents of a text file and puts it into a StringBuffer
         */
         public static StringBuffer textFileRead(String filename){
            StringBuffer sb = new StringBuffer();
            Logger logger = Logger.getLogger(Io.class);
            try{
                File file = new File(filename);
                char[] chars = new char[(int) file.length()];
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    for(int i = 0; i < 10000; i++) {
                        reader.read(chars);
                    }
                    reader.close();
                } catch (FileNotFoundException e) {
                    logger.error("", e);
                } catch (IOException e) {
                    logger.error("", e);
                }

                sb.append(new String(chars));
            } catch (Exception ex){
                logger.error("", ex);
            }
            return sb;
        }



        /**
         * Reads the contents of a text file and puts it into a StringBuffer
         */
         public static StringBuffer textFileRead(File file){
            StringBuffer sb = new StringBuffer();
            Logger logger = Logger.getLogger(Io.class);
            //File file = new File(filename);
            char[] chars = new char[(int) file.length()];
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                for(int i = 0; i < 10000; i++) {
                    reader.read(chars);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                logger.error("", e);
            } catch (IOException e) {
                logger.error("", e);
            }

            sb.append(new String(chars));

            return sb;
        }

        public static void writeTextToFile(File file, String text){
            writeTextToFile(file, text, false);
        }

        public static void writeTextToFile(File file, String text, boolean append){
            //file = new File("c:/temp/testwritetofile.txt");
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file, append));
                out.write(text);
                out.close();
            } catch (IOException e) {
            }
        }
        
        public static byte[] getBytesFromFile(File file) throws IOException {
            InputStream is = new FileInputStream(file);

            // Get the size of the file
            long length = file.length();

            if (length > Integer.MAX_VALUE) {
                // File is too large
            }

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int)length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "+file.getName());
            }

            // Close the input stream and return bytes
            is.close();
            return bytes;
        }


}

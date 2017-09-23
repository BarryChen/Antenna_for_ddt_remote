package com.make1.antenna.util;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CommandUtil {  
  
    public static final String TAG = CommandUtil.class.getSimpleName();  
    public static final String COMMAND_SH = "sh";  
    public static final String COMMAND_LINE_END = "\n";  
    public static final String COMMAND_EXIT = "exit\n";  
    private static final boolean ISDEBUG = true;  
    
    private static long firstTime = 0;
  

    public static List<String> execute(String command) {  
        return execute(new String[] { command });  
    }  
    

    public static boolean limitInput(long limit_time) {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > limit_time) {
            long time = secondTime - firstTime;
            firstTime = secondTime;
            Logger.e("entry --> limitInput() time === " + time + "  ---- >return true");
            return true;
        } else {
            long time = secondTime - firstTime;
            Logger.e("entry --> limitInput() time === " + time + "  ---- >return false");
            return false;
        }
    }

    public static List<String> execute(String[] commands) {  
        List<String> results = new ArrayList<String>();  
        int status = -1;  
        if (commands == null || commands.length == 0) {  
            return null;  
        }  
        debug("execute command start : " + commands);  
        Process process = null;  
        BufferedReader successReader = null;  
        BufferedReader errorReader = null;  
        StringBuilder errorMsg = null;  
  
        DataOutputStream dos = null;  
        try {  
            process = Runtime.getRuntime().exec(COMMAND_SH);
            dos = new DataOutputStream(process.getOutputStream());  
            for (String command : commands) {  
                if (command == null) {  
                    continue;  
                }  
                dos.write(command.getBytes());  
                dos.writeBytes(COMMAND_LINE_END);  
                dos.flush();  
            }  
            dos.writeBytes(COMMAND_EXIT);  
            dos.flush();  
  
            status = process.waitFor();  
  
            errorMsg = new StringBuilder();  
            successReader = new BufferedReader(new InputStreamReader(  
                    process.getInputStream()));  
            errorReader = new BufferedReader(new InputStreamReader(  
                    process.getErrorStream()));  
            String lineStr;  
            while ((lineStr = successReader.readLine()) != null) {  
                results.add(lineStr);  
                debug(" command line item : " + lineStr);  
            }  
            while ((lineStr = errorReader.readLine()) != null) {  
            	 results.add(lineStr);  
                errorMsg.append(lineStr);  
            }  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (dos != null) {  
                    dos.close();  
                }  
                if (successReader != null) {  
                    successReader.close();  
                }  
                if (errorReader != null) {  
                    errorReader.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
  
            if (process != null) {  
                process.destroy();  
            }  
        }  
        debug(String.format(Locale.CHINA,  
                "execute command end,errorMsg:%s,and status %d: ", errorMsg,  
                status));  
        return results;  
    }  

    private static void debug(String message) {  
        if (ISDEBUG) {  
            Log.d(TAG, message);  
        }  
    }  
    
    public static String bytesToHexString(byte src){
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == 0){  
            return null;  
        }  
            int v = src & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        return stringBuilder.toString();  
    }  
    

    public static void writeNode(String node , String writeStr) {
        try {
            if (!isFileExists(node)) {
                return;
            }
            FileOutputStream fout = new FileOutputStream(node);
            byte[] bytes = writeStr.getBytes();
            fout.write(bytes);
            fout.close();
            Logger.e( "Write success ->node:+"+node+"value:" + writeStr);
        } catch (FileNotFoundException e) {
        	Logger.e("FileNotFoundException-lee = " + e.toString());
        } catch (IOException e) {
        	Logger.e("IOException-lee = " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

}  
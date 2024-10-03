package ai.engineering;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.file.Paths;
import java.nio.file.Path;

public class SSHConnector{

    //Set the host IP, username, password, and port for training server
    static String host = "";
    static String user = "";
    static String password = "";
    static String port = ""

    //Set the local folder to hold temporary files
    static String local = "D:";
	
    static String remoteLoc="Main/";
    static String fileName = "/PerformanceTest.csv";

    static Session session;
    static Channel channel;

    public static Session openConnection(){
        try {
            java.util.Properties config = new java.util.Properties(); 
	        config.put("StrictHostKeyChecking", "no");
	        JSch jsch = new JSch();
	        Session session=jsch.getSession(user, host, port);
	        session.setPassword(password);
	        session.setConfig(config);
	        session.connect();
            return session;
        } catch (Exception e) {
            return null;
        }       
    }

    public static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    public static String ExecuteSSH(String command){

        String out = "";

        try {
            Session session = openConnection();
            Channel channel = session.openChannel("exec");

            ((ChannelExec) channel).setCommand(command);
            
            channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
                out = out + (new String(tmp, 0, i));
                System.out.println(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            //out = out + "exit-status: " + channel.getExitStatus();
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        channel.disconnect();
	        session.disconnect();
        } catch (Exception e) {
            out = "Command execution failed";
        }

        //System.out.println(out);
        return out;
    }

    public static String openSSH(){

        String out = "";

        try {
            session = openConnection();
            channel = session.openChannel("exec");
        } catch (Exception e) {
            out = "Command execution failed";
        }

        //System.out.println(out);
        return out;
    }

    public static String sendSSH(String command){

        String out = "";

        try {
            ((ChannelExec) channel).setCommand(command);
            
            channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
                out = out + (new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            out = out + "exit-status: " + channel.getExitStatus();
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
        } catch (Exception e) {
            out = "Command execution failed";
        }

        //System.out.println(out);
        return out;
    }

    public static String closeSSH(){

        String out = "";

        try {
	        channel.disconnect();
	        session.disconnect();
        } catch (Exception e) {
            out = "Command execution failed";
        }

        //System.out.println(out);
        return out;
    }

    public static void copyRemoteToLocal(){
        
        Session session = openConnection();

        String from = remoteLoc + File.separator + fileName;
        String prefix = null;
        String to = local;

        try{ 
            if (new File(to).isDirectory()) {
                prefix = to + File.separator;
            }

            // exec 'scp -f rfile' remotely
            String command = "scp -f " + from;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            while (true) {
                int c = checkAck(in);
                if (c != 'C') {
                    break;
                }

                // read '0644 '
                in.read(buf, 0, 5);

                long filesize = 0L;
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        // error
                        break;
                    }
                    if (buf[0] == ' ') break;
                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }

                String file = null;
            
                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        file = new String(buf, 0, i);
                        break;
                    }
                }

                System.out.println("Fetched " + from + ". Filesize: " + filesize);

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                // read a content of lfile
                FileOutputStream fos = new FileOutputStream(prefix == null ? to : prefix + file);
                int foo;
                while (true) {
                    if (buf.length < filesize) foo = buf.length;
                        else foo = (int) filesize;
                    foo = in.read(buf, 0, foo);
                    if (foo < 0) {
                        // error
                        break;
                    }
                    fos.write(buf, 0, foo);
                    filesize -= foo;
                    if (filesize == 0L) break;
                }

                if (checkAck(in) != 0) {
                    System.exit(0);
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                try {
                    if (fos != null) fos.close();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
        
                channel.disconnect();
                session.disconnect();
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void copyLocalToRemote(Session session, String from, String to, String fileName) throws JSchException, IOException {
        boolean ptimestamp = true;
        Path filePath = Paths.get(from, fileName);
        from = filePath.getFileName().toString();

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + to;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        File _lfile = new File(from);

        if (ptimestamp) {
            command = "T" + (_lfile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = _lfile.length();
        command = "C0644 " + filesize + " ";
        if (from.lastIndexOf('/') > 0) {
            command += from.substring(from.lastIndexOf('/') + 1);
        } else {
            command += from;
        }

        command += "\n";
        out.write(command.getBytes());
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        // send a content of lfile
        FileInputStream fis = new FileInputStream(from);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len); //out.flush();
        }

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }
        out.close();

        try {
            if (fis != null) fis.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        channel.disconnect();
        session.disconnect();
    }
}

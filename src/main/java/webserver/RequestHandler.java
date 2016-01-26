package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO �궗�슜�옄 �슂泥��뿉 ���븳 泥섎━�뒗 �씠 怨녹뿉 援ы쁽�븯硫� �맂�떎.
			
			InputStreamReader inputStreamReader = new InputStreamReader(in);
			BufferedReader in2 = new BufferedReader(inputStreamReader);
			
			
			String line=in2.readLine();
			
			if(line.contains("css"))
			{
				String[] cssTokens = line.split(" ");
				byte[] cssbody = Files.readAllBytes(new File("./webapp"+cssTokens[1]).toPath());
				DataOutputStream dos4 = new DataOutputStream(out);
				response200HeaderCss(dos4);
				response200BodyCss(dos4,cssbody);
			}
			
			if(line.contains("create?"))
			{
				int index = line.indexOf("?");
				String requestPath = line.substring(0, index);
				String params = line.substring(index+1);
				Map<String,String> parameters=util.HttpRequestUtils.parseQueryString(params);
				User user1 = new User(parameters.get("userId"),parameters.get("password"),parameters.get("name"),parameters.get("email"));
				return;
			}
			
			if(line.startsWith("POST"))
			{
				int contentlength3=0;
				while(!"".equals(line = in2.readLine()))
				{
					if(line == null){return;}
					
					if(line.contains("Content-Length"))
					{
						int index = line.indexOf(" ");
						contentlength3 = Integer.parseInt(line.substring(index+1));
					}
				}
				
				String body = util.IOUtils.readData(in2, contentlength3);
				Map<String,String> parameters=util.HttpRequestUtils.parseQueryString(body);
				User user1 = new User(parameters.get("userId"),parameters.get("password"),parameters.get("name"),parameters.get("email"));
				DataBase.addUser(user1);
				
				DataOutputStream dos3 = new DataOutputStream(out);
				response302Header(dos3);
				return;
			}
			
			if(line.contains("login?"))
			{
				int index = line.indexOf("?");
				String requestPath = line.substring(0, index);
				String params = line.substring(index+1);
				Map<String,String> loginparameters=util.HttpRequestUtils.parseQueryString(params);
				User curUser = DataBase.findUserById(loginparameters.get("userId"));
				
				DataOutputStream dos3 = new DataOutputStream(out);
				boolean login = true;
				if(curUser==null)
					login = false;
				
				response200HeaderCookie(dos3, login);
				byte[] body3 = Files.readAllBytes(new File("./webapp/index.html").toPath());
				response200BodyCookie(dos3,body3);
				
				return;
			}
			
			String[] tokens = line.split(" ");
			byte[] body2 = Files.readAllBytes(new File("./webapp" + tokens[1]).toPath());
			DataOutputStream dos2 = new DataOutputStream(out);
			response200Header(dos2,body2.length);
			responseBody(dos2,body2);
			
			while(!"".equals(line = in2.readLine()))
			{
				if(line == null){return;}
				System.out.println(line);
			}
			
			DataOutputStream dos = new DataOutputStream(out);
			byte[] body = "Hello World".getBytes();
			response200Header(dos, body.length);
			responseBody(dos, body);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		
		
	}
	
	

	private void response302Header(DataOutputStream dos){
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: http://localhost:8080/index.html");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void response200HeaderCss(DataOutputStream dos) {
		try {

			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void response200BodyCss(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void response200HeaderCookie(DataOutputStream dos, boolean login) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			if(login)
				dos.writeBytes("Set-Cookie: logined=" + "successful" + "\r\n");
			else
				dos.writeBytes("Set-Cookie: logined=" + "fail" + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void response200BodyCookie(DataOutputStream dos,byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}

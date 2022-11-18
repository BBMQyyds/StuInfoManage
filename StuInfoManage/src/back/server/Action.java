package back.server;

import back.data.Command;
import back.data.StudentList;
import back.utils.Utils;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

public class Action implements Runnable {

    //������
    BufferedReader br;
    //�����
    PrintWriter pw;
    //ѧ����Ϣ
    StudentList sl;
    //����ָ����Ϣ
    Stack<Command> cl;
    //����
    Command command;

    public Action(BufferedReader br, PrintWriter pw, StudentList sl, Stack<Command> cl) {
        this.br = br;
        this.pw = pw;
        this.sl = sl;
        this.cl = cl;
    }

    //��������
    public void receive() throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        while (!(line = br.readLine()).startsWith("Host")) {
            buffer.append(line);
        }

        Gson gson = new Gson();
        String json = Utils.getInstance().RchangeCode(
                StringUtils.substringBetween(buffer.toString(), "/?", " HTTP"));

        command = gson.fromJson(json, Command.class);

        System.out.println("receive " + json);
    }

    //��������
    public void feedback() {
        Gson gson = new Gson();
        String content = gson.toJson("null");
        boolean success = true;

        switch (command.getType()) {
            case "init" -> content = Utils.getInstance().FchangeCode(
                    Utils.getInstance().jsonSL(sl));
            case "create" -> content = gson.toJson(Utils.getInstance().createStudent(sl, command, cl));
            case "delete" -> content = gson.toJson(Utils.getInstance().deleteStudent(sl, command, cl));
            case "change" -> content = gson.toJson(Utils.getInstance().changeStudent(sl, command, cl));
        }

        if (content.equals("false")) {
            success = false;
            content = Utils.getInstance().FchangeCode(
                    Utils.getInstance().jsonSL(sl));
        }

        String html = "http/1.1 200 ok\r\n" +
                "Access-Control-Allow-Origin:*\r\n" +
                "Access-Control-Allow-Headers:*\r\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "Content-Length:" + content.length() +
                "\r\n\r\n" + content;
        pw.println(html);
        pw.flush();

        if (command.getType().equals("init") || !success) {
            System.out.println("return " + content.length() + "\n");
        } else {
            System.out.println("return " + content + "\n");
        }
    }

    //����
    public void run() {
        try {
            receive();
            feedback();

            pw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

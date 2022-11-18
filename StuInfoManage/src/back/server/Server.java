package back.server;

import back.data.Command;
import back.data.StudentList;
import back.utils.Utils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException {
        //�����˿ں�8088
        ServerSocket serverSocket = new ServerSocket(8088);
//        ExecutorService threadPool = new ThreadPoolExecutor(6, 12,
//                1L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(6), Executors.defaultThreadFactory());
        //�����̳߳�
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(6);
        //ѧ����Ϣ����
        StudentList sl = Utils.getInstance().readStuFile();
        //����ָ������
        Stack<Command> cl = Utils.getInstance().readComFile();

//        //��ѧ����Ϣ�����ļ�
//        Utils.getInstance().writeStuFile(sl);
//        //��ѧ����Ϣ�����ļ�
//        Utils.getInstance().writeComFile(cl);

        //���������ڿ���̨����ָ��ɽ��л��ˣ�rollback����һ��,rollbackall���˵���ʼ״̬��
        fixedThreadPool.execute(new Thread(new Rollback(sl, cl), "thread" + 0));

        //��������ͼ�λ�����
        Utils.getInstance().beautifyFrame();

        int i = 1;
        while (true) {

            //socket���󴴽�
            Socket socket = serverSocket.accept();
            //���������
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            //��ӡsocket��Ϣ
            System.out.println(socket);

            //ִ�д��߳�
            fixedThreadPool.execute(new Thread(new Action(br, pw, sl, cl), "thread" + i));

            //�����պͷ����ֱ𴴽������߳̿��ܻ���ɷ������ڽ���
//            //receive
//            fixedThreadPool.execute(new Thread(new ReceiveAction(br, sl, command), "thread" + i));
//            //feedback
//            fixedThreadPool.execute(new Thread(new FeedBackAction(pw, sl, command), "thread" + i + 1));

            //�߳���++
            i++;
        }
    }

}
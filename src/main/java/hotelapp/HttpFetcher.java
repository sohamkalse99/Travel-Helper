package hotelapp;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URL;

//class HttpFetcher
public class HttpFetcher {

    /**
     *
     * Returns data
     *
     * @param urlString
     *
     * @return data from get request in string format
     */
    public String getData(String urlString){
        String string = "";
        String stringWithoutHeader = "";

        PrintWriter out = null;
        BufferedReader in = null;
        SSLSocket socket = null;
        try {
            URL url = new URL(urlString);

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            socket = (SSLSocket) factory.createSocket(url.getHost(), 443);

            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String request = getRequest(url.getHost(), url.getPath() + "?" + url.getQuery());

            out.println(request); // send the request to the server
            out.flush();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                if (line.equals("0"))
                    continue;
                sb.append(line);
            }
            string = sb.toString();


        } catch (IOException e) {
            System.out.println(
                    "An IOException occured while writing to the socket stream or reading from the stream: " + e);
        } finally {
            try {
                // close the streams and the socket
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("An exception occured while trying to close the streams or the socket: " + e);
            }
        }

        return string;

    }

    /**
     * Returns data
     *
     * @param host
     * @param pathResourceQuery
     *
     * @return creates get request which would be sent to browser
     */
    private static String getRequest(String host, String pathResourceQuery) {
        String request = "GET " + pathResourceQuery + " HTTP/1.1" + System.lineSeparator() // GET
                // request
                + "Host: " + host + System.lineSeparator() // Host header required for HTTP/1.1
                + "Connection: close" + System.lineSeparator() // make sure the server closes the
                + System.lineSeparator();
        return request;
    }
}

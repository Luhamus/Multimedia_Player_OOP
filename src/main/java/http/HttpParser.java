package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

//Klass iga threadi päringu lugemiseks
public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    private static final int SP = 0x20; // 32  -> Space
    private static final int CR = 0x0D; // 13  -> Carriage Return( Nagu enter aga mitte päris)
    private static final int LF = 0x0A; // 10  -> Line feed ((Uus Rida)

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();
        try {
            parseRequestLine(reader, request);
            parseHeaders(reader, request);
            parseBody(reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }



        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        //Otsime Kohta kus on \cr\nf, see näitab requestLine lõppu.
        //Kui enne leiame SP characteri (Space), siis parsime vastavalt
        int _byte;
        while((_byte = reader.read()) >= 0){

            if(_byte == CR) {
                _byte = reader.read();
                if(_byte == LF) {
                    LOGGER.debug("Request Line VERSION to Process: {}" , processingDataBuffer.toString());
                    if(!methodParsed || !requestTargetParsed){
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    try {
                        request.setHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return;
                }else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            if (_byte == SP) {

                // Kuna Requestline jrk: Method, Req Target, Version, siis vaatame selles jrk.
                if (!methodParsed){
                    LOGGER.debug("Request Line METHOD to Process : {}" , processingDataBuffer.toString());
                    request.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                }else if (!requestTargetParsed){
                    LOGGER.debug("Request Line REQ TARGET to Process : {}" , processingDataBuffer.toString());
                    request.setRequestTarget(processingDataBuffer.toString());
                    requestTargetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }

                processingDataBuffer.delete(0, processingDataBuffer.length());
            }

            // Kui ei ole SP ega CR, siis lisame chari stringBuilderisse
            else{
                processingDataBuffer.append((char)_byte);
                if(!methodParsed){
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }

    /**
     * Reads bytes and adds lines(ifFinished == 2) to instance "request" until it detects 2 pairs of CR and LF (ifFinished == 4),
     * because that separates headers from body.
     * Reading bytes starts after start line, because last method already read them.
     *
     * @param reader stream of incoming bytes from request
     * @param request class where we store requests
     * @throws IOException
     * @throws HttpParsingException
     */
    private void parseHeaders(InputStreamReader reader, HttpRequest request)throws IOException, HttpParsingException {
        int _byte;
        StringBuilder processingDataBuffer = new StringBuilder();
        HashMap<String, String> headers = new HashMap<>();
        while ((_byte = reader.read()) >= 0) {
            int ifFinished = 0;
            while (_byte == CR || _byte == LF){
                ifFinished++;
                if(ifFinished == 2){
                    String[] headerLineArr=(processingDataBuffer.toString().split(":"));
                    headers.put(headerLineArr[0].strip(), headerLineArr[1].strip());
                    processingDataBuffer.setLength(0);
                }
                if(ifFinished == 4){
                    request.setHeaders(headers);
                    return;
                }

                _byte = reader.read();
            }
            processingDataBuffer.append((char)_byte);
            }
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) throws IOException {
        HashMap<String, String> headers = request.getHeaders();
        int bodyLength;
        StringBuilder processingDataBuffer = new StringBuilder();
        if(headers.containsKey("Content-Length")){
            bodyLength = parseInt(headers.get("Content-Length"));
        } else{
            return;
        }
        int _byte;
        for (int i = 0; i < bodyLength; i++) {
            _byte = reader.read();
            processingDataBuffer.append((char)_byte);
        }

        request.setBody(processingDataBuffer.toString());
        System.out.println(processingDataBuffer);
    }
}

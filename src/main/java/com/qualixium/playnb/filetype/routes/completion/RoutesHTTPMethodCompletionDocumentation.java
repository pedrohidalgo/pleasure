package com.qualixium.playnb.filetype.routes.completion;

import com.qualixium.playnb.util.ExceptionManager;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Action;
import org.netbeans.spi.editor.completion.CompletionDocumentation;

public class RoutesHTTPMethodCompletionDocumentation implements CompletionDocumentation {

    private final RoutesHTTPMethodCompletionItem item;

    public RoutesHTTPMethodCompletionDocumentation(RoutesHTTPMethodCompletionItem item) {
        this.item = item;
    }

    @Override
    public String getText() {
        StringBuilder textToReturn = new StringBuilder();
        switch (item.getText()) {
            case "GET":
                textToReturn.append("The GET method requests a representation of the specified resource. Requests using GET should only retrieve data and should have no other effect.");
                break;
            case "HEAD":
                textToReturn.append("The HEAD method asks for a response identical to that of a GET request, but without the response body. This is useful for retrieving meta-information written in response headers, without having to transport the entire content. This method is often used for testing hypertext links for validity, accessibility, and recent modification.");
                break;
            case "POST":
                textToReturn.append("The POST method requests that the server accept the entity enclosed in the request as a new subordinate of the web resource identified by the URI. The data POSTed might be, for example, an annotation for existing resources; a message for a bulletin board, newsgroup, mailing list, or comment thread; a block of data that is the result of submitting a web form to a data-handling process; or an item to add to a database.");
                break;
            case "PUT":
                textToReturn.append("The PUT method requests that the enclosed entity be stored under the supplied URI. If the URI refers to an already existing resource, it is modified; if the URI does not point to an existing resource, then the server can create the resource with that URI.");
                break;
            case "DELETE":
                textToReturn.append("The DELETE method deletes the specified resource.");
                break;
            case "TRACE":
                textToReturn.append("The TRACE method echoes the received request so that a client can see what (if any) changes or additions have been made by intermediate servers.");
                break;
            case "OPTIONS":
                textToReturn.append("The OPTIONS method returns the HTTP methods that the server supports for the specified URL. This can be used to check the functionality of a web server by requesting '*' instead of a specific resource.");
                break;
            case "CONNECT":
                textToReturn.append("The CONNECT method converts the request connection to a transparent TCP/IP tunnel, usually to facilitate SSL-encrypted communication (HTTPS) through an unencrypted HTTP proxy.");
                break;
            case "PATCH":
                textToReturn.append("The PATCH method applies partial modifications to a resource.");
                break;
        }
        return textToReturn.toString();
    }

    @Override
    public URL getURL() {
        try {
            return new URL("https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol#Request_methods");
        } catch (MalformedURLException ex) {
            ExceptionManager.logException(ex);
        }

        return null;
    }

    @Override
    public CompletionDocumentation resolveLink(String string) {
        return null;
    }

    @Override
    public Action getGotoSourceAction() {
        return null;
    }
}

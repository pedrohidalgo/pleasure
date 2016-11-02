package com.qualixium.playnb.unit.scalatemplate;

import com.qualixium.playnb.filetype.scalatemplate.format.ScalaTemplateReformatTask;
import com.qualixium.playnb.util.MiscUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScalaTemplateFormattingTest {

    private static String scalaTemplateFileContent;

    @BeforeClass
    public static void setUpClass() throws URISyntaxException, IOException {
        URI uri = ScalaTemplateFormattingTest.class.getClassLoader().getResource("ScalaTemplateTemplate.scala.html").toURI();
        scalaTemplateFileContent = new String(Files.readAllBytes(Paths.get(uri)));
    }

    @Test
    public void testFormatFile() {
        int spaces = 4;
        final String contentToVerifyLocation = "<div id=\"first_div_row\" class=\"row\">";
        String fileFormatted = ScalaTemplateReformatTask.formatFile(scalaTemplateFileContent, spaces);

        List<String> listLines = MiscUtil.getLinesFromFileContent(fileFormatted);
        listLines.forEach(line -> {
            if (line.contains(contentToVerifyLocation)) {
                assertEquals(spaces, line.indexOf(contentToVerifyLocation));
            }
        });
    }

    @Test
    public void testFormatFileContentWithOutHTML() {
        int spaces = 4;
        final String fileContent = "@(message: String)\n"
                + "\n"
                + "@main(\"Welcome to Play\") {\n"
                + "@play20.welcome(message, style = \"Java\")\n"
                + "\n"
                + "    \n"
                + "}";
        final String contentToVerifyLocation = "@play20.welcome(message, style = \"Java\")";
        String fileFormatted = ScalaTemplateReformatTask.formatFile(fileContent, spaces);

        List<String> listLines = MiscUtil.getLinesFromFileContent(fileFormatted);
        listLines.forEach(line -> {
            if (line.contains(contentToVerifyLocation)) {
                assertEquals(spaces, line.indexOf(contentToVerifyLocation));
            }
        });
    }

    @Test
    public void testFormatComplexHeadFileContent() {
        int spaces = 4;
        final String fileContent
                = "@(title: String = \"\", description: String = \"\")(content: Html)\n"
                + "\n"
                + "@import be.objectify.deadbolt.java.views.html._\n"
                + "\n"
                + "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "\n"
                + "    <head>\n"
                + "        <title>BookInApp</title>\n"
                + "\n"
                + "        <meta charset=\"utf-8\">\n"
                + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                + "        <meta name=\"description\" content=\"\">\n"
                + "        <meta name=\"author\" content=\"\">\n"
                + "\n"
                + "        <!-- Twitter Card data -->\n"
                + "        <meta name=\"twitter:site\" content=\"http://bookinapp.com/\">\n"
                + "        <meta name=\"twitter:title\" content=\"@title\">\n"
                + "        <meta name=\"twitter:description\" content=\"@description\">\n"
                + "\n"
                + "        <!-- Open Graph data -->\n"
                + "        <meta property=\"og:type\" content=\"article\" />\n"
                + "        <meta property=\"og:title\" content=\"@title\" />\n"
                + "        <meta property=\"og:url\" content=\"http://bookinapp.com/\" />\n"
                + "        <meta property=\"og:description\" content=\"@description\" />\n"
                + "\n"
                + "        @if(!title.isEmpty){\n"
                + "        <meta property=\"og:title\" content=\"BookInApp\" />\n"
                + "        <meta name=\"twitter:title\" content=\"BookInAPP\">\n"
                + "\n"
                + "        }\n"
                + "        @if(!description.isEmpty){\n"
                + "        <meta property=\"og:description\" content=\"Check this out.\" />\n"
                + "        <meta name=\"twitter:description\" content=\"Check this out.\">\n"
                + "\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        <!--Favicon-->\n"
                + "        <link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"@routes.Assets.at(\"images/favicon.ico\")\">\n"
                + "\n"
                + "              <!-- Bootstrap Core CSS -->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/bootstrap-cerulean.css\")\">\n"
                + "\n"
                + "              <!-- Theme Custom Fonts -->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/theme/font-awesome.min.css\")\">\n"
                + "\n"
                + "              <!-- Theme CSS -->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/jquery-ui.min.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/jquery-ui.structure.min.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/jquery-ui.theme.min.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/theme/sb-admin.css\")\">\n"
                + "\n"
                + "              <!-- Theme Plugins CSS -->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/theme/plugins/time-picker.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/theme/plugins/social-buttons.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/theme/plugins/metisMenu.css\")\">\n"
                + "\n"
                + "              <!--Custom CSS-->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/animate.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/main.css\")\">\n"
                + "\n"
                + "\n"
                + "    </head>\n"
                + "</html>";
        final String contentToVerifyLocation = "<meta property=\"og:title\" content=\"BookInApp\" />";
        String fileFormatted = ScalaTemplateReformatTask.formatFile(fileContent, spaces);

        List<String> listLines = MiscUtil.getLinesFromFileContent(fileFormatted);
        listLines.forEach(line -> {
            if (line.contains(contentToVerifyLocation)) {
                assertEquals(spaces * 3, line.indexOf(contentToVerifyLocation));
            }
            if (line.contains("}")) {
                assertEquals(spaces * 2, line.indexOf("}"));
            }
        });
    }

    @Test
//    @Ignore
    public void testFormatVeryComplexFileContent() {
        int spaces = 4;
        final String fileContent
                = "@(title: String = \"\", description: String = \"\")(content: Html)\n"
                + "\n"
                + "@import be.objectify.deadbolt.java.views.html._\n"
                + "\n"
                + "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "\n"
                + "    <body>\n"
                + "   @subjectPresent() {\n"
                + "        @defining(TheUser.getAuthenticatedUser().get()) { user =>\n"
                + "\n"
                + "        <div id=\"wrapper\">\n"
                + "\n"
                + "            <!-- Navigation -->\n"
                + "            <nav class=\"navbar navbar-default navbar-static-top\" role=\"navigation\" style=\"margin-bottom: 0\">\n"
                + "                <div class=\"navbar-header\">\n"
                + "                    <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">\n"
                + "                        <span class=\"sr-only\">Toggle navigation</span>\n"
                + "                        <span class=\"icon-bar\"></span>\n"
                + "                        <span class=\"icon-bar\"></span>\n"
                + "                        <span class=\"icon-bar\"></span>\n"
                + "                    </button>\n"
                + "                    <a class=\"navbar-brand\" href=\"@routes.MainController.index()\">\n"
                + "                        <img class=\"logo\" src=\"@routes.Assets.at(\"images/logo.jpg\")\" alt=\"logo\"/>\n"
                + "                    </a>\n"
                + "\n"
                + "                </div>\n"
                + "                <!-- /.navbar-header -->\n"
                + "\n"
                + "                <ul class=\"nav navbar-top-links navbar-right\">\n"
                + "\n"
                + "                    <li class=\"dropdown\">\n"
                + "                        @if(!user.isOwner()){\n"
                + "                        @defining(Notification.findForDashBoard()){ listNotifications =>\n"
                + "                    <li class=\"dropdown  @if(!listNotifications.isEmpty()){ dropdown-newmail }\">\n"
                + "                        <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n"
                + "                            <i class=\"fa fa-envelope fa-fw\"></i>\n"
                + "                            <span class=\" @{if(Notification.findForDashBoard().isEmpty) \"invisible\" else \"notification\"}\">\n"
                + "                                  @Notification.getTotalByUser()\n"
                + "                            </span>\n"
                + "                            <i class=\"fa fa-caret-down\"></i>\n"
                + "                        </a>\n"
                + "                        <ul class=\"dropdown-menu dropdown-messages\">\n"
                + "                            @for(notification <- listNotifications){\n"
                + "                            <li>\n"
                + "                                <a href=\"@controllers.crud.routes.NotificationController.showDetail(notification.id)\">\n"
                + "                                    <div>\n"
                + "                                        <strong>@notification.title</strong>\n"
                + "                                        <span class=\"pull-right text-muted\">\n"
                + "                                            <em>@notification.insertionDate.format(util.DateUtil.FORMAT_DATE_PATTERN_VIEW)</em>\n"
                + "                                        </span>\n"
                + "                                    </div>\n"
                + "                                </a>\n"
                + "                            </li>\n"
                + "                            <li class=\"divider\"></li>\n"
                + "                            }\n"
                + "                            <li>\n"
                + "                                <a class=\"text-center\" href=\"@controllers.crud.routes.NotificationController.listCustomersNotifications()\">\n"
                + "                                    <strong>Read Notifications</strong>\n"
                + "                                    <i class=\"fa fa-angle-right\"></i>\n"
                + "                                </a>\n"
                + "                            </li>\n"
                + "                        </ul>\n"
                + "                        <!-- /.dropdown-messages -->\n"
                + "                    </li>\n"
                + "                    }\n"
                + "                    }\n"
                + "                    </li>\n"
                + "                    <li><a href=\"@routes.ProfileController.profilePage()\"><i class=\"fa fa-user fa-fw\"></i> Welcome, @user.email<i class=\"fa fa-fw fa-caret-down\"></i>\n"
                + "                        </a>\n"
                + "                    </li>\n"
                + "                    <li><a href=\"@routes.MainController.logout()\"><i class=\"fa fa-sign-out fa-fw\"></i> Logout</a></li>\n"
                + "                </ul>\n"
                + "                <!-- /.navbar-top-links -->\n"
                + "                <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->\n"
                + "                <div class=\"navbar-default sidebar\" role=\"navigation\">\n"
                + "                    <div class=\"sidebar-nav navbar-collapse\">\n"
                + "                        <ul class=\"nav\" id=\"side-menu\">\n"
                + "                            <li>\n"
                + "                                <a href=\"@routes.MainController.index()\"><i class=\"fa fa-fw fa-dashboard\"></i> Dashboard</a>\n"
                + "                            </li>\n"
                + "                            <li><a href=\"@routes.ProfileController.profilePage()\"><i class=\"fa fa-user fa-fw\"></i> My Profile</i></a></li>\n"
                + "                            @dynamic(\"\", Permission.BO_CUSTOMERS){\n"
                + "                            <li><a href=\"@routes.TheUserController.listBOCustomers()\"><i class=\"fa fa-fw fa-users\"></i>My Customers</a></li>\n"
                + "                            }\n"
                + "                            @dynamic(\"\", Permission.MANT_OFFER){\n"
                + "                            <li><a href=\"@controllers.crud.routes.OfferController.list()\"><i class=\"fa fa-fw fa-certificate\"></i>Offers</a></li>\n"
                + "                            }\n"
                + "                            @dynamic(\"\", Permission.MANT_SERVICE){\n"
                + "                            <li><a href=\"@controllers.crud.routes.BusinessServiceController.list()\"><i class=\"fa fa-fw fa-gear\"></i>Services</a></li>\n"
                + "                            }\n"
                + "                            @dynamic(\"\", Permission.MANT_APPOINTMENT){\n"
                + "                            <li><a href=\"@controllers.crud.routes.AppointmentController.list()\"><i class=\"fa fa-fw fa-calendar\"></i>Appointments</a></li>\n"
                + "                            }\n"
                + "                            @dynamic(\"\", Permission.VIEW_APPOINTMENTS){\n"
                + "                            <li><a href=\"@controllers.crud.routes.AppointmentController.listCustomersAppointments()\"><i class=\"fa fa-fw fa-calendar\"></i>Customer Appointments</a></li>\n"
                + "                            }\n"
                + "                            @dynamic(\"\", Permission.MANT_NOTIFICATION){\n"
                + "                            <li><a href=\"@controllers.crud.routes.NotificationController.list()\"><i class=\"fa fa-fw fa-bell\"></i>Notifications</a></li>\n"
                + "                            }\n"
                + "                            <li><a href=\"@routes.MainController.changePassword()\"><i class=\"fa fa-lock fa-fw\"></i>Change Password</a></li>\n"
                + "                        </ul>\n"
                + "                    </div>\n"
                + "                </div>\n"
                + "                <!-- /.navbar-collapse -->\n"
                + "            </nav>\n"
                + "\n"
                + "            <div id=\"page-wrapper\">\n"
                + "\n"
                + "                <div class=\"container-fluid\">\n"
                + "\n"
                + "                    <!-- Page Heading -->\n"
                + "\n"
                + "                    <div class=\"main_content\">\n"
                + "                        @if(flash.contains(\"success\")) {\n"
                + "                        <p class=\"alert alert-success\">\n"
                + "                            <a href=\"#\" class=\"close\" data-dismiss=\"alert\">&times;</a>\n"
                + "                            @flash.get(\"success\")\n"
                + "                        </p>\n"
                + "                        }\n"
                + "\n"
                + "                        @if(flash.contains(\"error\")) {\n"
                + "                        <p class=\"alert alert-danger\">\n"
                + "                            <a href=\"#\" class=\"close\" data-dismiss=\"alert\">&times;</a>\n"
                + "                            @flash.get(\"error\")\n"
                + "                        </p>\n"
                + "                        }\n"
                + "                        @content\n"
                + "                    </div>\n"
                + "\n"
                + "                </div>\n"
                + "                <!-- /.container-fluid -->\n"
                + "\n"
                + "            </div>\n"
                + "            <!-- /#page-wrapper -->\n"
                + "\n"
                + "        </div>\n"
                + "        }\n"
                + "        <!-- /#wrapper -->\n"
                + "\n"
                + "        <!-- jQuery -->\n"
                + "\n"
                + "        <!--jquery theme js-->\n"
                + "        <script src=\"@routes.Assets.at(\"javascripts/jquery.min.js\")\" type=\"text/javascript\"></script>\n"
                + "        <script src=\"@routes.Assets.at(\"javascripts/jquery-ui.min.js\")\" type=\"text/javascript\"></script>\n"
                + "\n"
                + "        <!-- Bootstrap Core JavaScript -->\n"
                + "        <script src=\"@routes.Assets.at(\"javascripts/bootstrap.min.js\")\" type=\"text/javascript\"></script>\n"
                + "\n"
                + "        <!--Theme JS-->\n"
                + "        <script src=\"@routes.Assets.at(\"javascripts/theme/plugins/metisMenu.js\")\" type=\"text/javascript\"></script>\n"
                + "        <script src=\"@routes.Assets.at(\"javascripts/theme/sb-admin-2.js\")\" type=\"text/javascript\"></script>\n"
                + "        <script src=\"@routes.Assets.at(\"javascripts/theme/plugins/timePicker.js\")\" type=\"text/javascript\"></script>\n"
                + "        <script src=\"@routes.Assets.at(\"javascripts/main.js\")\" type=\"text/javascript\"></script>\n"
                + "\n"
                + "        }\n"
                + "        @subjectNotPresent(){\n"
                + "        <script>\n"
                + "            window.location = '/login';\n"
                + "        </script>\n"
                + "        }\n"
                + "\n"
                + "        <script>\n"
                + "            (function (i, s, o, g, r, a, m) {\n"
                + "                i['GoogleAnalyticsObject'] = r;\n"
                + "                i[r] = i[r] || function () {\n"
                + "                    (i[r].q = i[r].q || []).push(arguments)\n"
                + "                }, i[r].l = 1 * new Date();\n"
                + "                a = s.createElement(o),\n"
                + "                        m = s.getElementsByTagName(o)[0];\n"
                + "                a.async = 1;\n"
                + "                a.src = g;\n"
                + "                m.parentNode.insertBefore(a, m)\n"
                + "            })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');\n"
                + "\n"
                + "            ga('create', 'UA-37659676-17', 'auto');\n"
                + "            ga('send', 'pageview');\n"
                + "\n"
                + "        </script>\n"
                + "\n"
                + "    </body>\n"
                + "\n"
                + "</html>";

        final String contentToVerifyLocation = "<script src=\"@routes.Assets.at(\"javascripts/jquery.min.js\")\" type=\"text/javascript\"></script>";
        final String arrobaDefiningLine = "@defining(TheUser.getAuthenticatedUser().get()) { user =>";
        String fileFormatted = ScalaTemplateReformatTask.formatFile(fileContent, spaces);

        List<String> listLines = MiscUtil.getLinesFromFileContent(fileFormatted);
        listLines.forEach(line -> {
            if (line.contains(arrobaDefiningLine)) {
                assertEquals(spaces * 3, line.indexOf(arrobaDefiningLine));
            }
            if (line.contains(contentToVerifyLocation)) {
                assertEquals(spaces * 2, line.indexOf(contentToVerifyLocation));
            }
        });
    }

    @Test
    public void testFormatFileHadBug() {
        int spaces = 4;
        final String fileContent
                = "@(emp: Employee, myVar: String, listEmps: List[Employee],numbers: List[Int])\n"
                + "\n"
                + "this is the @myVar.toLowerCase @emp.age\n"
                + "\n"
                + "<input type=\"text\" value=\"@emp.name\" />\n"
                + "\n"
                + "@for(obj <- listEmps){\n"
                + "this text\n"
                + "}";
        final String contentToVerifyLocation = "this text";
        String fileFormatted = ScalaTemplateReformatTask.formatFile(fileContent, spaces);

        List<String> listLines = MiscUtil.getLinesFromFileContent(fileFormatted);
        listLines.forEach(line -> {
            if (line.contains(contentToVerifyLocation)) {
                assertEquals(spaces, line.indexOf(contentToVerifyLocation));
            }
        });
    }

    @Test
    public void testFormatFileHadBug2() {
        int spaces = 4;
        final String fileContent
                = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <meta charset=\"utf-8\">\n"
                + "        <title>Appointments - Qualixium</title>\n"
                + "        <link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"@routes.Assets.at(\"images/favicon.ico\")\">\n"
                + "\n"
                + "              <!-- Bootstrap Core CSS -->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/bootstrap-cerulean.css\")\">\n"
                + "\n"
                + "              <!-- Theme CSS -->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/jquery-ui.min.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/jquery-ui.theme.min.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/jquery-ui.structure.min.css\")\">\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/theme/sb-admin.css\")\">\n"
                + "              <!-- Theme Custom Fonts -->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/theme/font-awesome.min.css\")\">\n"
                + "\n"
                + "              <!--Custom CSS-->\n"
                + "              <link rel=\"stylesheet\" media=\"screen\" href=\"@routes.Assets.at(\"stylesheets/login.css\")\">\n"
                + "\n"
                + "              <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        @if(flash.contains(\"success\")) {\n"
                + "        <p class=\"alert alert-success\">\n"
                + "            @flash.get(\"success\")\n"
                + "        </p>\n"
                + "        }\n"
                + "\n"
                + "        @if(flash.contains(\"error\")) {\n"
                + "        <div class=\"alert alert-danger\" role=\"alert\">\n"
                + "            <span class=\"fa glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>\n"
                + "            <span class=\"sr-only\">Error:</span>\n"
                + "            @flash.get(\"error\")\n"
                + "        </div>\n"
                + "        }\n"
                + "        <div class=\"container\">\n"
                + "            <div class=\"row\">\n"
                + "                <div class=\"col-md-offset-4 col-lg-4\">\n"
                + "                    <div class=\"form-login\">\n"
                + "                        <img class=\"img-responsive text-center\" src=\"@routes.Assets.at(\"images/logo.jpg\")\" alt=\"logo\"/>\n"
                + "\n"
                + "                             @helper.form(action = routes.MainController.recoverPassword()) {\n"
                + "\n"
                + "                             <div class=\"form-group input-group\">\n"
                + "                            <span class=\"input-group-addon\"><i class=\"fa fa-envelope\"></i> </span>\n"
                + "                            <input type=\"email\" id=\"email\" name=\"email\" class=\"form-control input-lg chat-input\"\n"
                + "                                   placeholder=\"Email\" required autofocus />\n"
                + "                        </div>\n"
                + "                        <button type=\"submit\" class=\"btn btn-primary btn-lg btn-block\">Recover</button> \n"
                + "                        <div class=\"row\">\n"
                + "                            <a href=\"@routes.MainController.login()\" class=\"btn btn-info btn-outline pull-left btn-lg\"><i class=\"fa  fa-long-arrow-left\"></i> Login</a>\n"
                + "                        </div>                        \n"
                + "                        }\n"
                + "                    </div>\n"
                + "                </div>\n"
                + "            </div>\n"
                + "        </div>\n"
                + "    </body>\n"
                + "</html>";
        
        final String contentToVerifyLocation = "<div class=\"container\">";
        String fileFormatted = ScalaTemplateReformatTask.formatFile(fileContent, spaces);

        List<String> listLines = MiscUtil.getLinesFromFileContent(fileFormatted);
        listLines.forEach(line -> {
            if (line.contains(contentToVerifyLocation)) {
                assertEquals(spaces * 2, line.indexOf(contentToVerifyLocation));
            }
        });
    }

    @Test
    public void testFormatOneLineBlock() {
        int spaces = 4;
        final String fileContent = "<li class=\"dropdown  @if(!listNotifications.isEmpty()){ dropdown-newmail }\">";
        String fileFormatted = ScalaTemplateReformatTask.formatFile(fileContent, spaces);

        assertEquals(fileContent + LINE_SEPARATOR, fileFormatted);
    }

    @Test
    public void testFormatFileMapAfterOpenBracketNewLine() {
        int spaces = 4;
        final String fileContent
                = "@(emp: Employee, myVar: String, listEmps: List[Employee],numbers: List[Int])\n"
                + "\n"
                + "this is the @myVar.toLowerCase @emp.age\n"
                + "\n"
                + "<input type=\"text\" value=\"@emp.name\" />\n"
                + "\n"
                + "@numbers map { nu => another text\n"
                + "this text\n"
                + "}";
        final String contentToVerifyLocation = "@numbers map { nu =>";
        final String newLineText = "another text";
        String fileFormatted = ScalaTemplateReformatTask.formatFile(fileContent, spaces);

        List<String> listLines = MiscUtil.getLinesFromFileContent(fileFormatted);

        assertTrue(listLines.contains(contentToVerifyLocation));
        listLines.forEach(line -> {
            if (line.contains(newLineText)) {
                assertEquals(spaces, line.indexOf(newLineText));
            }
        });
    }

    @Test
    public void testGetOpenTagsAmount() {
        final String line = "<div>";

        int openTagsAmount = ScalaTemplateReformatTask.getOpenTagsAmount(line);

        assertEquals(1, openTagsAmount);
    }

    @Test
    public void testGetCloseTagsAmount() {
        final String line = "</div>";
        int expected = -1;
        int actual = ScalaTemplateReformatTask.getOpenTagsAmount(line);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetsTagsAmountOpenAndClose() {
        final String line = "<div>something you know </div>";
        int expected = 0;
        int actual = ScalaTemplateReformatTask.getOpenTagsAmount(line);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetsTagsAmountOpen_2_Close_1() {
        final String line = "<div>something you know </div><b>";
        int expected = 1;
        int actual = ScalaTemplateReformatTask.getOpenTagsAmount(line);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetsTagsAmountTagAutoClose() {
        final String line = "<input type=\"text\" value=\"@emp.name\" />";
        int expected = 0;
        int actual = ScalaTemplateReformatTask.getOpenTagsAmount(line);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetsTagsAmountWithUnClosedHTML() {
        final String line = "<!DOCTYPE html>";
        int expected = 0;
        int actual = ScalaTemplateReformatTask.getOpenTagsAmount(line);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetsTagsAmountWithComment() {
        final String line = "<!--Theme JS-->";
        int expected = 0;
        int actual = ScalaTemplateReformatTask.getOpenTagsAmount(line);

        assertEquals(expected, actual);
    }

}

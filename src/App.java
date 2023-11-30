import java.io.IOException;
import java.io.Serializable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javafx.scene.web.WebHistory;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class App extends Application implements Serializable {

    int source_status = 0;
    String str ="http://google.com";

    public static void main(String[] args) throws Exception {
        launch(args);
    }
    Image icon = new Image("pic/icon.png");
    
    
    double H = 540 , W = 960,zoom=1.0;
    
    BorderPane borderPane = new BorderPane();
    SplitPane splitPane = new SplitPane();
    WebView webView = new WebView();
    WebHistory history ;
    HBox hbox = new HBox(8);

    TextArea textarea = new TextArea();
    TextField textField = new TextField();
    Button BtLoad = new Button("Search");
    Button BtSource = new Button("source");
    Button BtLEFT = new Button("<-");
    Button BtRIGHT = new Button("->");
    Button Btzoomin = new Button("+");
    Button Btzoomout = new Button("-");
    Button Btreset = new Button("reset");

    @Override
    public void start(Stage primaryStage) throws Exception {

        webView.getEngine().load(str);
        splitPane.getItems().add(webView);
        textField.setText(str);

        //LEFT
        
        Image imageL = new Image("pic/left.png");
        ImageView BtLEFT = new ImageView(imageL);
        BtLEFT.setFitHeight(40);
        BtLEFT.setFitWidth(40);
        hbox.getChildren().add(BtLEFT);

        //RIGHT
        Image image = new Image("pic/right.png");
        ImageView BtRIGHT = new ImageView(image);
        BtRIGHT.setFitHeight(40);
        BtRIGHT.setFitWidth(40);
        hbox.getChildren().add(BtRIGHT);
        
        //textField
        
        hbox.getChildren().add(textField);
        textField.setPrefSize(400, 35);
        textField.setStyle(
        "-fx-background-radius: 10;" +
        "-fx-background-color: #999999;" +
        "-fx-text-fill: white;");
       
        //load
        
        Image imageIoad = new Image("pic/load.png");
        ImageView BtLoad = new ImageView(imageIoad);
        BtLoad.setFitHeight(40);
        BtLoad.setFitWidth(40);
        hbox.getChildren().add(BtLoad);
        

        //source
        
        Image imageS = new Image("pic/source.png");
        ImageView BtSource = new ImageView(imageS);
        BtSource.setFitHeight(40);
        BtSource.setFitWidth(70);
        BtSource.setStyle(
        "-fx-background-radius: 50;");
        hbox.getChildren().add(BtSource);
        
        //in
        
        Image imageIn = new Image("pic/z_in.png");
        ImageView Btzoomin = new ImageView(imageIn);
        Btzoomin.setFitHeight(40);
        Btzoomin.setFitWidth(40);
        hbox.getChildren().add(Btzoomin);

        //out
        
        Image imageOut = new Image("pic/z_out.png");
        ImageView Btzoomout = new ImageView(imageOut);
        Btzoomout.setFitHeight(40);
        Btzoomout.setFitWidth(40);
        hbox.getChildren().add(Btzoomout);
        
        //Btreset
        Image imageRe = new Image("pic/reset.png");
        ImageView Btreset = new ImageView(imageRe);
        Btreset.setFitHeight(40);
        Btreset.setFitWidth(70);
        hbox.getChildren().add(Btreset);

        hbox.setStyle(
        "-fx-background-color: #CCCCFF;" );
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
       
        
        borderPane.setOnKeyPressed(e->{
            if(e.getCode()==KeyCode.ENTER)
            {
                try {
                    String url = textField.getText();
                    webView.getEngine().load(url);
                    str = url;
                    history = webView.getEngine().getHistory(); 
                    System.out.println(str);
                    if(splitPane.getItems().size()==2)
                    {
                        Document document = Jsoup.connect(str).timeout(3000).userAgent("Mozilla").get();// .data("name","jsoup")
                        textarea.appendText(document.outerHtml());
                        textarea.setScrollTop(0);
                        textarea.setScrollLeft(0);
                    }
                }
                catch (Exception ex) {
                    System.out.println(e.toString());
                }
            }
            else if(e.getCode()==KeyCode.ESCAPE)
            {
                if(splitPane.getItems().size()==2)
                    splitPane.getItems().remove(1);
            }
            else if(e.getCode()==KeyCode.F5)
            {
                webView.getEngine().reload();
            }
        });
        
        BtLEFT.setOnMouseClicked(e->{
            try {
                back();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        
        BtRIGHT.setOnMouseClicked(e->{
            try {
                forward();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        BtLoad.setOnMouseClicked(e -> {
            try {
                String url = textField.getText();
                webView.getEngine().load(url);
                str = url;
                history = webView.getEngine().getHistory();
                Stage stage = (Stage) textField.getScene().getWindow();
                ObservableList<WebHistory.Entry> entries = history.getEntries();
                stage.setTitle(entries.get(history.getCurrentIndex()).getTitle());
                if(splitPane.getItems().size()==3)
                {
                    Document document = Jsoup.connect(str).timeout(3000).userAgent("Mozilla").get();// .data("name","jsoup")
                    textarea.appendText(document.outerHtml());
                    textarea.setScrollTop(Double.MIN_VALUE);
                    textarea.setScrollLeft(0);
                }
            }
            catch (Exception ex) {
                System.out.println(e.toString());
            }
        });
        
        
        BtSource.setOnMouseClicked(e -> {
            try {
                if(source_status == 1){
                    source_status = 0;
                    splitPane.getItems().remove(1);
                } else {
                source_status = 1;
                textarea.clear();
                textarea.setEditable(false);
                System.out.println(splitPane.getItems().size());
                if(splitPane.getItems().size()==1)
                    splitPane.getItems().add(textarea);
                    Document document = Jsoup.connect(str).timeout(3000).userAgent("Mozilla").get();// .data("name","jsoup")
                    textarea.appendText(document.outerHtml());
                    textarea.setScrollTop(0.0);
                    textarea.setScrollLeft(0);
                }
            } catch (Exception ex) {
                System.out.println(e.toString());
            }
        });
        
        Btzoomin.setOnMouseClicked(e -> {
            zoom+=0.10;
            webView.setZoom(zoom);
        });
        
        Btzoomout.setOnMouseClicked(e -> {
            zoom-=0.10;
            webView.setZoom(zoom);
        });

        Btreset.setOnMouseClicked(e -> {
            zoom=1.0;
            webView.setZoom(zoom);
        });

        

        borderPane.setTop(hbox);
        borderPane.setCenter(splitPane);

        //display
        Scene scene = new Scene(borderPane, W, H);
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Chlam Browzing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    void back() throws IOException
    {       
        history = webView.getEngine().getHistory(); 
        history.go(-1);
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        str = entries.get(history.getCurrentIndex()).getUrl();
        textField.setText(str);
        if(splitPane.getItems().size()==2)
        {
            Document document = Jsoup.connect(str).timeout(3000).userAgent("Mozilla").get();// .data("name","jsoup")
            textarea.appendText(document.outerHtml());
            textarea.setScrollTop(Double.MIN_VALUE);
            textarea.setScrollLeft(0);
        }
    }
    void forward() throws IOException
    {       
        history = webView.getEngine().getHistory(); 
        history.go(1);
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        str = entries.get(history.getCurrentIndex()).getUrl();
        textField.setText(str);
        if(splitPane.getItems().size()==2)
        {
            Document document = Jsoup.connect(str).timeout(3000).userAgent("Mozilla").get();// .data("name","jsoup")
            textarea.appendText(document.outerHtml());
            textarea.setScrollTop(Double.MIN_VALUE);
           textarea.setScrollLeft(0);
        }
    }
} 


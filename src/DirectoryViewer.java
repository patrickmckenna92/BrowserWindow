import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;

public class DirectoryViewer extends Application {

    @Override
    public void start(Stage primaryStage) {
        TreeView<File> treeView = new TreeView<>();
        BorderPane borderPane = new BorderPane();

        Player player = new Player();
        treeView.setRoot(getNodesForDirectory(new File("/home/patrick/Downloads")));
        treeView.getRoot().setExpanded(true);
        HBox hBox = new HBox();

        javafx.scene.control.Button play = new Button("Play");
        javafx.scene.control.Button pause = new javafx.scene.control.Button("Pause");
        javafx.scene.control.Button stop = new javafx.scene.control.Button("Stop");

        play.setOnAction(e -> player.play());
        pause.setOnAction(e -> player.pause());
        stop.setOnAction(e -> player.stop());

        hBox.getChildren().addAll(play, pause, stop);
        borderPane.setBottom(hBox);
        borderPane.setCenter(treeView);


        // Only display the filename not the entire path.
        treeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            public TreeCell<File> call(TreeView<File> t) {
                return new TreeCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        setText((empty || item == null) ? "" : item.getName());
                    }
                };
            }
        });

        //add listeners to tree to play wav files.
        treeView.getSelectionModel().selectedItemProperty()
                .addListener((observable, old_val, new_val) -> {
                    TreeItem<File> selectedItem = new_val;
                    if (getFileExtension(selectedItem.getValue().getName()).equals("wav")) {
                        System.out.println("Playing: " + selectedItem.getValue().getName());
                        player.loadClip(new File(selectedItem.getValue().getAbsolutePath()));
                        player.play();
                    }
                });
        primaryStage.setScene(new Scene(borderPane, 600, 400));
        primaryStage.setTitle("Folder View");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Creates a tree structure of a users directories from a given root.
     * It will filter out any file type that is not ".wav"
     * Adds event handlers to the wav files so that they can be previewed.
     *
     * @param directory
     * @return root
     */
    public TreeItem<File> getNodesForDirectory(File directory) { //Returns a TreeItem representation of the specified directory
        TreeItem<File> root = new TreeItem<>(directory);
        for (File f : directory.listFiles()) {
            if (f.isDirectory()) { //Then we call the function recursively
                //add try catch statement to load complete directory.
                root.getChildren().add(getNodesForDirectory(f));
                System.out.println("Loading " + f.getName());
            } else if (getFileExtension(f.getName()).equals("wav")) {
                TreeItem<File> wav = new TreeItem<>(f);
                root.getChildren().add(wav);
                System.out.println("Loading " + f.getName());
            }
        }
        return root;
    }

    /**
     * Returns the file type. E.g. File "Fixuplooksharp.mp3" will return "mp3".
     * @param fullName - the files name.
     * @return - the files type
     */
    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}

package application;

import javafx.fxml.FXML;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class HomeController {
	@FXML
	private TextArea result;
	@FXML
	private Button saveBtn;
	@FXML
	private TextArea inputText;
	@FXML
	private ImageView writeImg;
	@FXML
	private ImageView readImg;
	
	private BitMatrix matrix;
	private double winx,winy;
	@FXML
	public void createQR(ActionEvent event) {
		String txt = inputText.getText();
		if(txt.isEmpty()) return;
		QRCodeWriter writer = new QRCodeWriter();
		try {
			BitMatrix matrix = writer.encode(txt, BarcodeFormat.QR_CODE, 400, 400);
			this.matrix = matrix;
			BufferedImage img = MatrixToImageWriter.toBufferedImage(matrix);
			Image fimg = SwingFXUtils.toFXImage(img, null);
			writeImg.setImage(fimg);
			saveBtn.setDisable(false);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void animateSmall(MouseEvent evt) {
		Button btn = (Button)evt.getSource();
		btn.setRotate(10);
		
	}
	@FXML
	public void animateBig(MouseEvent evt) {
		Button btn = (Button)evt.getSource();
		btn.setRotate(0);
	}
	
	@FXML
	public void copyClip(ActionEvent event) {
		Clipboard board = Clipboard.getSystemClipboard();
		Image img = board.getImage();
		if(img==null) {
			return;
		}
		readQR(img);
		readImg.setImage(img);
	}
	@FXML
	public void saveBtnAction(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		File file = chooser.showSaveDialog(((Button)(event.getSource())).getScene().getWindow());
		if(file==null) return;
		try {
			MatrixToImageWriter.writeToFile(matrix, "png", file);
			saveBtn.setDisable(true);
			writeImg.setImage(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void openClicked(ActionEvent event) {
		FileChooser choose = new FileChooser();
		File file = choose.showOpenDialog(((Button) event.getSource()).getScene().getWindow());
		if (file == null)
			return;
		try {
			readImg.setImage(new Image(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		result.setText(file.getName());
		readQR(file);
	}
	@FXML
	public void windowClose(MouseEvent evt) {
		Platform.exit();
	}
	@FXML
	public void windowMinimize(MouseEvent evt) {
		((Stage)((ImageView)evt.getSource()).getScene().getWindow()).setIconified(true);
	}
	@FXML
	public void windowPressed(MouseEvent evt) {
		winx = ((Stage)((Rectangle)evt.getSource()).getScene().getWindow()).getX() - evt.getScreenX();
	    winy = ((Stage)((Rectangle)evt.getSource()).getScene().getWindow()).getY() - evt.getScreenY();
	}
	@FXML
	public void windowDragged(MouseEvent evt) {
		((Stage)((Rectangle)evt.getSource()).getScene().getWindow()).setX(evt.getScreenX() + winx);
		((Stage)((Rectangle)evt.getSource()).getScene().getWindow()).setY(evt.getScreenY() + winy);
	}
	


	private void readQR(File file) {
		try {
			BufferedImage img = ImageIO.read(file);
			LuminanceSource src = new BufferedImageLuminanceSource(img);
			HybridBinarizer binary = new HybridBinarizer(src);
			BinaryBitmap map = new BinaryBitmap(binary);
			try {
				Result res = new MultiFormatReader().decode(map);
				result.setText(res.getText());
			} catch (Exception ex) {
				result.setText("No QR Code Found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void readQR(Image fximg) {
		BufferedImage img = SwingFXUtils.fromFXImage(fximg, null);
		LuminanceSource src = new BufferedImageLuminanceSource(img);
		HybridBinarizer binary = new HybridBinarizer(src);
		BinaryBitmap map = new BinaryBitmap(binary);
		try {
			Result res = new MultiFormatReader().decode(map);
			result.setText(res.getText());
		} catch (Exception ex) {
			result.setText("No QR Code Found");
		}
	}
}

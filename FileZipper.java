import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

public class FileZipper{
	static final int BUFFER = 1024;

	public static void main(String[] args){
		//Constructor
		while(true){
			getOption();
		}
	}

	private static void getOption(){
		Scanner userInput = new Scanner(System.in);
		System.out.println("Would you like to Zip or Unzip?");
		String result = userInput.nextLine();
		
		if(Objects.equals(result,"Zip")){
			zipFile();
		} else if (Objects.equals(result, "zip")) {
			zipFile();
		} else if (Objects.equals(result, "Unzip")) {
			unzipFile();
		} else if (Objects.equals(result, "unzip")) {
			unzipFile();
		}

	}

	private static void zipFile(){
		ZipOutputStream zipOutputStream = null;
		BufferedInputStream bufferedInputStream = null;
		Scanner fileInput = new Scanner(System.in);
		System.out.println("Enter the path of the file to zip");
		String filePath = fileInput.nextLine();

		try{
			/*Can only put buffer variables in buffered streams
			If no buffered streams are used, then do not use the offset and len parameters in the
			read() function.
			 */
			File file = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream, BUFFER);
			Scanner zipFileInput = new Scanner(System.in);
			System.out.println("Enter a name for the zip archive");
			String zipPath = zipFileInput.nextLine();
			File zipFile = new File(zipPath);
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile); //Change this path everytime you move the folder
			zipOutputStream = new ZipOutputStream(fileOutputStream);

			ZipEntry zipEntry = new ZipEntry(file.getName()); //Declares specified file to the zip archive
			zipOutputStream.putNextEntry(zipEntry); //Adds specified file to the zip archive

			byte data[] = new byte[BUFFER];
			int count;
			while((count = bufferedInputStream.read(data, 0, BUFFER)) > 0){
				zipOutputStream.write(data, 0, count);
			}
			System.out.println("Archived " + filePath + " to " + zipFile.getAbsolutePath());
			try{
				Thread.sleep(2000);
				System.out.println("\n");
			}catch (InterruptedException ex){
				new RuntimeException(ex);
			}

			System.out.println("\n");
		}catch(IOException IOError){
			System.out.println("Error zipping file :" + IOError.getMessage());
		} finally{
			if(zipOutputStream != null){
				try{
					zipOutputStream.close();
				}catch(IOException IOError){
					IOError.printStackTrace();
				}
			}
			if(bufferedInputStream != null){
				try{
					bufferedInputStream.close();
				}catch(IOException IOError){
					IOError.printStackTrace();
				}
			}
		}
	}

	public static void unzipFile(){
		Scanner zipLocation = new Scanner(System.in);
		System.out.println("Enter path of archive to unzip");
		String zipFilename = zipLocation.nextLine();
		File zipFile = new File(zipFilename);
		Scanner extractLocation = new Scanner(System.in);
		System.out.println("Enter extraction location");
		String destinationLocation = extractLocation.nextLine();

		File destinationDirectory = new File(destinationLocation);
		if(!destinationDirectory.exists()){
			destinationDirectory.mkdirs();
		}
		try {
			/*Can only put buffer variables in buffered streams
			If no buffered streams are used, then do not use the offset and len parameters in the
			read() function.
			 */
			FileInputStream fileInputStream = new FileInputStream(zipFile);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, BUFFER);
			ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream); //Cannot put a buffer in here
			ZipEntry zipEntry = zipInputStream.getNextEntry();
			while (zipEntry != null) {
				byte[] buffer = new byte[BUFFER];
				String fileName = zipEntry.getName();
				File newFile = new File(destinationLocation + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				new File(String.valueOf(newFile.getParentFile().mkdirs()));
				FileOutputStream fileOutputStream = new FileOutputStream(newFile);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				int len = 0;
				while ((len = zipInputStream.read(buffer, 0, len)) > 0) {
					bufferedOutputStream.write(buffer, 0, len);
				}
				bufferedOutputStream.close();
				zipInputStream.closeEntry();
				zipEntry = zipInputStream.getNextEntry();
			}
			zipInputStream.closeEntry();
			fileInputStream.close();
			zipInputStream.close();
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("Error unzipping file " + fileNotFoundException.getMessage());
			destinationDirectory.delete();
			try {
				Thread.sleep(2000);
				System.out.println("\n");
			} catch (InterruptedException ex) {
				throw new RuntimeException(ex);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws IOException {
		String filename = "temp";
		PrintWriter writer = new PrintWriter(filename + ".csv", "UTF-8");
		String fline = "year,month,day,hour,minute,second,temp";
		writer.println(fline);
		
		
		for (int y = 2013; y <= 2014; y++) {
			for (int m = 1; m <= 12; m++) {
				for (int d = 1; d <= 31; d++) {
					String dd = intToString(d, 2);
					String mm = intToString(m, 2);
					String yy = y+"";
					
					String res = parseURLDay(dd, mm, yy);
					System.out.print(res);
					writer.print(res);
				}
			if(m>7 & y==2014)
				break;
			}
		}

		writer.close();
		
//		String d = intToString(8, 2);
//		String m = intToString(6, 2);
//		String y = "2014";
//	
//		String res = parseURLDay(d, m, y);
//		System.out.println(res);
	}

	private static String parseURLDay(String d, String m, String y) throws MalformedURLException, IOException {
		String result = "";
		// meteo, velVento, hr, pres, pp, solar
		URL url = new URL(
				"http://meteo.ist.utl.pt/private/ema/emaEstat.php?param=temp&day="+d+"&month="+m+"&year="+y+"&period=daily");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null){
			String res = parseLine(inputLine);
			if(!res.equals(""))
				result += parseLine(inputLine) + "\n";
		}
			
		in.close();
		
		return result;
	}

	private static String parseLine(String inputLine) {
		String result = "";
		if (inputLine
				.contains("<script type='text/javascript'>var VARS_AMBIENTE = new Array()")) {
			//System.out.println(inputLine);

			String[] content = inputLine.split("VARS_AMBIENTE");
			for (int i = 0; i < content.length; i++) {
				//System.out.println(content[i]);
				if(content[i].contains("['data']")){
					
					String res = content[i];
					res = res.replace("\t", "");
					res = res.replace(" ", "");
					res = res.split("=")[1];
					
					
					res = res.replace(",[", ";");
					res = res.replace("),", ",");
					res = res.replace("[[", "");
					res = res.replace("]];", "");
					
					
					res = res.replace(";", "\n");
					
					res = res.replace("Date.UTC(", "");
					
					res = res.replace("]", "");
					
					
					result +=res;
				}
					
					
			}
		}
		
		return result;

	}
	
	static String intToString(int num, int digits) {
	    assert digits > 0 : "Invalid number of digits";

	    // create variable length array of zeros
	    char[] zeros = new char[digits];
	    Arrays.fill(zeros, '0');
	    // format number as String
	    DecimalFormat df = new DecimalFormat(String.valueOf(zeros));

	    return df.format(num);
	}

}

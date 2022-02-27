import java.sql.*;
import java.io.*;
import java.util.Scanner;

public class MySQLJDBC {

	public static void main(String[] args) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			Scanner sc = new Scanner(System.in);
			File file1 = new File("movie_data.txt");
			BufferedReader br1=new BufferedReader(new FileReader(file1));

			File file2 = new File("create_table.txt");
			BufferedReader br2=new BufferedReader(new FileReader(file2));

			String line="";
			String[] data1;

			//Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "son699399");

			//Execute a query
			stmt = conn.createStatement();

			int input_num; //메뉴 번호 선택
			int count = 0; //릴레이션 추가했는지의 여부 확인
			String query = ""; //데이터 추가할때 쓰는 query
			String q_date = "select * from movie where (releasedate between ";  //query 문을 전달할때 쓸 문자열
			int audience; //관객수
			String title; //제목
			String date; // 날짜
			String date2;// 두번째 날짜
			do {
				System.out.println("==========================================");
				System.out.println("(0)종료");
				System.out.println("(1)릴레이션 생성 및 데이터 추가");
				System.out.println("(2)제목을 이용한 검색");
				System.out.println("(3)관객수를 이용한 검색");
				System.out.println("(4)개봉일을 이용한 검색");
				System.out.println("==========================================");
				System.out.println("원하는 번호를 입력하시오");
				input_num = sc.nextInt();
				switch(input_num) {
					case 0:
						System.out.println("프로그램 종료.");
						break;
					case 1:
						if(count==0) {
							while ((line = br2.readLine()) != null)
								query += line;
							stmt.executeUpdate(query);

							while ((line = br1.readLine()) != null) {
								String data = "insert into movie values('";
								data1 = line.split("[|]");
								for (int i = 1; i < data1.length; i++) {
									data += data1[i];
									if (i != data1.length - 1)
										data += "','";
									else
										data += "');";
								}
								stmt.executeUpdate(data);
								count++;
							}
							System.out.println("데이터 추가 완료.");
						}
						else{
							System.out.println("데이터가 이미 존재합니다.");
						}
						break;
					case 2:
						System.out.print("사용자 입력:");
						title = sc.next();
						rs = stmt.executeQuery("select * from movie where title like '%"+title+"%';");
						//Print results
						while(rs.next()) {
							System.out.println("|"+rs.getString("id") + "|" + rs.getString("title") + "|" + rs.getString("company") + "|" + rs.getString("releasedate")+"|"+ rs.getString("country")+"|"+ rs.getString("totalscreen")+"|"+ rs.getString("profit")+"|"+ rs.getString("totalnum")+"|"+ rs.getString("grade")+"|");
						}
						break;
					case 3:
						System.out.print("사용자 입력:");
						audience = sc.nextInt();
						sc.nextLine();
						rs = stmt.executeQuery("select * from movie where totalnum >"+ Integer.toString(audience)+";");
						//Print results
						while(rs.next()) {
							System.out.println("|"+rs.getString("id") + "|" + rs.getString("title") + "|" + rs.getString("company") + "|" + rs.getString("releasedate")+"|"+ rs.getString("country")+"|"+ rs.getString("totalscreen")+"|"+ rs.getString("profit")+"|"+ rs.getString("totalnum")+"|"+ rs.getString("grade")+"|");
						}
						break;
					case 4:
						System.out.print("사용자 입력:");
						date = sc.next();
						date2 = sc.next();
						date = date.replace(",","");
						q_date += "'"+date2+"' and '"+date+"');";
						System.out.println(q_date);
						rs = stmt.executeQuery(q_date);
						while(rs.next()) {
							System.out.println("|"+rs.getString("id") + "|" + rs.getString("title") + "|" + rs.getString("company") + "|" + rs.getString("releasedate")+"|"+ rs.getString("country")+"|"+ rs.getString("totalscreen")+"|"+ rs.getString("profit")+"|"+ rs.getString("totalnum")+"|"+ rs.getString("grade")+"|");
						}
						break;
					default:
						System.out.println("입력한 숫자 범위 오류, 0~4 사이의 숫자를 입력하세요.");
						break;
				}
			}while(input_num != 0);



//			rs = stmt.executeQuery("SELECT * FROM instructor");

			//Print results
//            while(rs.next()) {
//            	System.out.println(rs.getString("dept_name") + "|" + rs.getString(2) + "|" + rs.getString(3) + "|" + rs.getDouble("salary"));
//
//            }


            // close a connection
            stmt.close();
            conn.close();

		}catch (SQLException ex) {
			//Handle errors for JDBC
			ex.printStackTrace();
		} catch (Exception e){
		    //Handle errors for Class.forName
			e.printStackTrace();
		}
	}
}
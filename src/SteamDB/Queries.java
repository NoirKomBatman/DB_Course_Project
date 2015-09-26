package SteamDB;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import SteamDB.SteamMenu;

public class Queries extends SteamMenu {
	public Queries()  {
		super();
	}
	
	public ArrayList<Game> getAllGamesSQL() 
	{
		ArrayList<Game> retList1 = new ArrayList<Game>();
		try{
			
			String query = "select r.appID, r.title, r.genre, r.developmentDate, p.devName from GameRemoves r, GamePublishes p where r.appID = p.appID and r.isRemoved ='N'";
			
			Statement st = con.createStatement();
			
			ResultSet r = st.executeQuery(query);
			
			while(r.next())
			{
				Game tempura = new Game(
					r.getInt(1),
					r.getString(2),
					r.getString(3),
					r.getDate(4),
					r.getString(5));
				retList1.add(tempura);
			}
		
		}
		catch(SQLException e){
			System.out.println("ERROR");		
		}
		return retList1;
	
	}
	
	public ArrayList<String> getFriends(int playerID){
		ArrayList<String> friendList = new ArrayList<String>();	
		try{
		String query = "select p.playerID, p.name from Player p where p.playerID IN ((select f.playerID2 from Friends f where "+ playerID + 
				"= f.playerID1) UNION (select f.playerID1 from Friends f where f.playerID2 = " +
				playerID + "))";
		
		Statement r = con.createStatement();
		
		ResultSet rs = r.executeQuery(query);
		while(rs.next()){
			String temp = rs.getInt(1) + " , " + rs.getString(2);
			friendList.add(temp);
		}
				    
	}
		catch( SQLException e){
			System.out.println("ERROR");
		}
		return friendList;

}
	public ArrayList<Player> getGameChampion(int appID){
		ArrayList<Player> players = new ArrayList<Player>();
		try{
			String query = "select distinct p.playerID, p.name,p.email, p.userLvl from Player p,Obtains o where o.appID = "
					+ appID + " and p.playerID = o.playerID and not exists (select achievementName from Achievements a1 where a1.appID = "+ appID +" MINUS "
					+ "(select a.achievementName from Achievements a where a.appID = "+ appID+ "))";
			Statement r = con.createStatement();
			
			ResultSet rs = r.executeQuery(query);
			
			while(rs.next()){
				Player temp = new Player(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
				players.add(temp);
			}
					    
		}
		catch(SQLException e){
			System.out.println("ERROR");
			
		}
		return players;

	}		
	
	public int countPlayerWithAchievement(String achievementName, int appID){
		int i = 0;
		try{
			String query = "select count (distinct o.playerID) from Obtains o, Achievements a where "
					+ "a.achievementName= "+ achievementName +"and a.appID= "+ appID + "a.appID = o.appID";
			Statement r = con.createStatement();
			
			ResultSet rs = r.executeQuery(query);
			
			while(rs.next()){
				i = rs.getInt(1);
			}
		}
			
			catch(SQLException e){
				System.out.println("countPlayerWithAchievementError");
			}
			return i;
		}
	
	public boolean deleteFriends(int from, int todelete){
		try{
			Statement r = con.createStatement();
			String delete = "delete from Friends f where (f.playerID1 = " + from + " and f.playerID2 = " + todelete+")";
			String delete1 = "delete from Friends f where (f.playerID2 = " + from + " and f.playerID1 = " + todelete+")";
			int ret = r.executeUpdate(delete);
			int ret1 = r.executeUpdate(delete1);
			if (ret != 0) return true;
			else return false;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public boolean deleteAdmin(int adminID){
		try{
			Statement r = con.createStatement();
			String delete = "delete from Administrator where adminID = " + adminID;
			int ret =r.executeUpdate(delete);
			if(ret != 0) return true;
			else return false;
		}
		catch(SQLException e){
			System.out.println("Error in deleting Administrator");
			return false;
		}
	}
	
	public ArrayList<Player> getNotBannedPlayer(){
		ArrayList<Player> retList = new ArrayList<Player>();
		try{
			String query = "select p.playerID, p.name,p.email, p.userLvl from Player p where p.playerID"
					+ " not in (select b.playerID from BannedPlayer b)";
			Statement r = con.createStatement();
			ResultSet rs = r.executeQuery(query);
			
			while(rs.next()){
				Player temp = new Player(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
				retList.add(temp);
			}
			
		}
		catch (SQLException e){
			System.out.println("Cant show player");
		}		
		return retList;		
	}
	
	public String getMostExpensiveDev(){
		String toReturn = null;
		try{
			Statement r = con.createStatement();
			String query = "Create View devavg (devName, price) as (select p.devName as devName, avg(r.price) as price from GamePublishes p, GameRemoves r where p.appID = r.appID "
					+ " group by p.devName)";		
			r.executeQuery(query);
			String query2 = "select devName, d.price from devavg d where d.price = (select max(price)"
					+ "from devavg)";
			ResultSet rs = r.executeQuery(query2);
			
							
			while(rs.next()){
				toReturn = rs.getString(1) +"   $"+ rs.getFloat(2);
				
			}
			r.executeQuery("drop view devavg");
		}
		
		catch(SQLException e){
			System.out.println("Cant get most expensive developer");
		}
		
		return toReturn;
	}
	public String getCheapestDev(){
		String toReturn = null;
		try{
			Statement r = con.createStatement();
			String query = "Create View devavg (devName, price) as (select p.devName as devName, avg(r.price) as price from GamePublishes p, GameRemoves r where p.appID = r.appID "
					+ " group by p.devName)";		
			r.executeQuery(query);
			String query2 = "select devName, d.price from devavg d where d.price = (select min(price)"
					+ "from devavg)";
			ResultSet rs = r.executeQuery(query2);
					
			while(rs.next()){
				toReturn = rs.getString(1) +"    $"+ rs.getFloat(2);
			}
			r.executeQuery("drop view devavg");
		}
		
		catch(SQLException e){
			System.out.println("Cant get cheapest developer");
		}
		
		return toReturn;
	}
	
	public boolean changePassword(int playerID, String newpass){
		try{
			Statement r = con.createStatement();
			String query = "Update Player p set password ='" + newpass + "' where p.playerID = " + playerID;
		    r.executeUpdate(query);
		}
		catch(SQLException e){
			System.out.println("ERROR");
			return false;
		}	
		return true;
	}
	
	public boolean addPlayer(int playerID, String name, String email, String password){
		try{
			String query = "insert into Player values ("+ playerID+",'" + name + "','" + email + "','" +  password + "'," +"1)";
			Statement r = con.createStatement();
			r.executeUpdate(query);
		}
		catch(SQLException e){
			System.out.println("insert new players fail");
			return false;
		}
		return true;
	}
	
	public boolean addGame(int appID, String genre, float price, String title, String devDate, String devName){
		try{
			String query = "insert into GameRemoves values ("+ appID +",'"+genre +"',"+ price + "," + "'N','" + title + "'"+
					",to_date('"+devDate+"','DD-MM-YYYY')" + ", 0 , null, null)";
			Statement r = con.createStatement();			
			String query2 = "insert into GamePublishes values("+ appID + ",'" + devName +"')";			
			String query3 = "insert into Developer values('" + devName+ "')";
			r.executeUpdate(query3);
			r.executeUpdate(query2);
			r.executeUpdate(query);
		}
		catch( SQLException e){
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	// THIS IS QUERY FOR LOGIN SCREEN!!!! IF WHAT WE CAN USE PLAYERID AND PASSWORD FOR AUTHORIZATION
		public HashMap<String, String> getCombinations() {
			HashMap<String, String> combinations = new HashMap<String, String>();
			try{
				String query = "select email, password from Player";
				Statement r = con.createStatement();
				
				ResultSet rs = r.executeQuery(query);
				
				while(rs.next()){
					String email = rs.getString(1);
					String password = rs.getString(2);
					combinations.put(email, password);
				}
						    
			}
			catch(SQLException e){
				System.out.println("ERROR");
				
			}
			return combinations;
		}
		
		public int chckAdmin(String email, String pass) {
			int i = -1; 
			try{
				String query = "select adminID from Administrator, Player where playerID = adminID and email ='"+ email +"' and password ='"+ pass+"'";
				Statement r = con.createStatement();
				
				ResultSet rs = r.executeQuery(query);
				while(rs.next()){
					i = rs.getInt(1);
				};
						    
			}
			catch(SQLException e){
				super.errorWindow("CHCK ADMIN ERROR");
				
			}
			return i;
		}
		
		public int chckValveDev(String email, String pass) {
			int i = -1;
			try{
				String query = "select v.valveDevID from ValveDeveloper v, Player p where p.playerID = v.valveDevID and p.email = '"+ email +"' and p.password = '"+ pass +"'";
				Statement r = con.createStatement();
				
				ResultSet rs = r.executeQuery(query);
				while(rs.next()){
					i = rs.getInt(1);
				};	    
			}
			catch(SQLException e){
				super.errorWindow("CHCKVALVEDEV ERROR");
				
			}
			return i;
		}
	
		
		
		public int chckPlayer(String email, String pass) {
			int i = -1;
			try{
				String query = "select p.playerID from Player p where  p.email = '"+ email +"' and p.password = '"+ pass +"'";
				Statement r = con.createStatement();
				
				ResultSet rs = r.executeQuery(query);
				while(rs.next()){
					i = rs.getInt(1);
				};	    
			}
			catch(SQLException e){
				super.errorWindow("CHCK PLAYER ERROR");
				
			}
			return i;
		}
		
		public boolean banPlayer(int playerID, int adminID, String banDate, String banReason) {
			try {
				String query = "insert into BannedPlayer values ("+playerID+","+adminID+",to_date('"+banDate+"','DD-MM-YYYY'), '"+banReason+"')";
				Statement r = con.createStatement();
				
				 r.execute(query);
			} 
			catch (SQLException e)
			{
				super.errorWindow(e.getMessage());
				return  false;
			}
			return true;
		}
		
		public boolean banGame(int appID){
			try{
				String query = "update GameRemoves set isRemoved = 'Y' where appID ="+ appID;
				Statement r = con.createStatement();
				int ret = r.executeUpdate(query);
				if (ret != 0) return true;
				else return false;
			}
			
			catch(SQLException e){
				super.errorWindow(e.getMessage());	
				return false;
			}
		}
		
		public int getMaxAppID(){
			int i = -1;
			try{
				String query = "select max(appID) from GameRemoves";
				Statement r = con.createStatement();
				ResultSet rs = r.executeQuery(query);
				while(rs.next()){
				  i = rs.getInt(1);
				}
			}
				catch(SQLException e){
					super.errorWindow("Can't get max appID");
				}
			return i;
				
			}
		
		public int getMaxPlayerID(){
			int i = -1;
			try{
				String query = "select max(playerID) from Player";
				Statement r = con.createStatement();
				ResultSet rs = r.executeQuery(query);
				while(rs.next()){
				  i = rs.getInt(1);
				}
			}
				catch(SQLException e){
					super.errorWindow("Can't get max playerID");
				}
			return i;
				
			}
		
		public ArrayList<Integer> getAllValveID(){
			ArrayList<Integer> retList = new ArrayList<Integer>();
			
			try{
				String query = "select valveDevID from ValveDeveloper";
				Statement r = con.createStatement();
				ResultSet rs = r.executeQuery(query);
				while(rs.next()){
					int i = rs.getInt(1);
					retList.add(i);
				}
			
			}
			
			catch( SQLException e){
				super.errorWindow(e.getMessage());
			}
			return retList;
			
		}
		
		public ArrayList<Integer> getOnlyAdminID(){
			ArrayList<Integer> retList = new ArrayList<Integer>();
			
			try{
				String query = "select a.adminID from Administrator a where a.adminID MINUS (select v.valveDevID from ValveDeveloper)";
				Statement r = con.createStatement();
				ResultSet rs = r.executeQuery(query);
				while(rs.next()){
					int i = rs.getInt(1);
					retList.add(i);
				}
			
			}
			
			catch( SQLException e){
				super.errorWindow(e.getMessage());
			}
			return retList;
			
		}
		
		public ArrayList<Player> getBannedPlayer(){
			ArrayList<Player> retList = new ArrayList<Player>();
			try{
				String query = "select p.playerID, p.name,p.email, p.userLvl from Player p where p.playerID"
						+ " in (select b.playerID from BannedPlayer b)";
				Statement r = con.createStatement();
				ResultSet rs = r.executeQuery(query);
				
				while(rs.next()){
					Player temp = new Player(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
					retList.add(temp);
				}
				
			}
			catch (SQLException e){
				System.out.println("Cant show player");
			}		
			return retList;		
		}
		
		public boolean addFriend(String date,int f1, int f2) {
			try{
				String query = "insert into Friends values (TO_DATE('"+date+"','DD-MM-YYYY'),'"+f1+"','"+f2+"')";
				String query1 = "insert into Friends values (TO_DATE('"+date+"','DD-MM-YYYY'),'"+f2+"','"+f1+"')";
				Statement r = con.createStatement();
				r.execute(query);
				r.execute(query1);
				
			}
			catch (SQLException e){
				System.out.println("Cant add friend");
				return false;
			}
			return true;
		}
		
		public String getPlayerName(int id) {
			String name = null;
			try{
				String query = "select name from Player where playerID="+id;
				Statement r = con.createStatement();
				ResultSet rs = r.executeQuery(query);
				
				while(rs.next())
				{
					name = rs.getString(1);
				}
			}
			catch (SQLException e){
				System.out.println("Cant get Player");
			}
			return name;
		}
		
		public ArrayList<Game> getGamesOwned(int id) {
			ArrayList<Game> retList1 = new ArrayList<Game>();
			try{
				
				String query = "select r.appID, r.title, r.genre, o.since, p.devName from GameRemoves r, GamePublishes p, Owns o where r.appID = p.appID and o.playerID = "+id+" and o.appID = p.appID and r.isRemoved ='N'";
				
				Statement st = con.createStatement();
				
				ResultSet r = st.executeQuery(query);
				
				while(r.next())
				{
					Game tempura = new Game(
						r.getInt(1),
						r.getString(2),
						r.getString(3),
						r.getDate(4),
						r.getString(5));
					retList1.add(tempura);
				}
			
			}
			catch(SQLException e){
				super.errorWindow(e.getMessage());	
			}
			return retList1;
		}
		
		public ArrayList<Item> getItemsCollected(int id) {
			ArrayList<Item> retList1 = new ArrayList<Item>();
			try{
				
				String query = "select i.itemID, i.appID, g.title, i.itemLvl, i.description from ItemBelongs i, Collects c, GameRemoves g where c.playerID="+id+" and c.itemID = i.itemID and i.appID = g.appID";
				
				Statement st = con.createStatement();
				
				ResultSet r = st.executeQuery(query);
				
				while(r.next())
				{
					Item tempura = new Item(
						r.getString(1),
						r.getInt(2),
						r.getString(3),
						r.getInt(4),
						r.getString(5));
					retList1.add(tempura);
				}
			
			}
			catch(SQLException e){
				super.errorWindow(e.getMessage());
			}
			return retList1;
		}
		
		public ArrayList<Achievement> getAchievementsObtained(int id) {
			ArrayList<Achievement> retList1 = new ArrayList<Achievement>();
			try{
				
				String query = "select a.achievementName, a.description, g.title  from Achievements a, Obtains o, GameRemoves g where o.playerID="+id+" and o.achievementName=a.achievementName and o.appID=g.appID";
				
				Statement st = con.createStatement();
				
				ResultSet r = st.executeQuery(query);
				
				while(r.next())
				{
					Achievement tempura = new Achievement(
						r.getString(1),
						r.getString(2),
						r.getString(3));
					retList1.add(tempura);
				}
			
			}
			catch(SQLException e){
				super.errorWindow(e.getMessage());	
			}
			return retList1;
		}
		
		public ArrayList<News> getNews(){
			ArrayList<News> retList = new ArrayList<News>();
			try{
				String query = "select g.appID, g.title, n.message, n.publishDate from GameRemoves g, News n , Mentions m where"
						+ " m.appID = g.appID and m.newsID = n.newsID";
				Statement st = con.createStatement();
				ResultSet r = st.executeQuery(query);
				
				while(r.next()){
					News temp = new News(
							r.getInt(1),
							r.getString(2),
							r.getString(3),
							r.getDate(4));
					retList.add(temp);
				}
			}
			catch(SQLException e){
				super.errorWindow(e.getMessage());
			}
			
			return retList;
		}
		
		public ArrayList<String> getView() {
			ArrayList<String> toReturn = new ArrayList<String>();
			try{
				Statement r = con.createStatement();
				String query = "Create View devavg (devName, price) as (select p.devName as devName, avg(r.price) as price from GamePublishes p, GameRemoves r where p.appID = r.appID "
						+ " group by p.devName)";		
				String query1 = "select devName,price from devavg ";		
				r.executeQuery(query);
		
				ResultSet rs = r.executeQuery(query1);
						
				while(rs.next()){
					String tempura = rs.getString(1)+", "+rs.getString(2);
					toReturn.add(tempura);
				}
				r.executeQuery("drop view devavg");
			}
			
			catch(SQLException e){
				System.out.println("Cant get view");
			}
			
			return toReturn;
		}
		
	}
		
		


		
	



		
	



		
	



		
	



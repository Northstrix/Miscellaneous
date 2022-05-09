import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.sql.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.util.Scanner;
import java.security.SecureRandom;
/*
A weird, bootleg database that utilizes the blockchain technology
Works in the semi-manual mode
Utilizes SQLite
You can find more information here https://github.com/Northstrix/Miscellaneous
Distributed under the MIT License
© Copyright Maxim Bortnikov 2022
 */
public class MainClass extends JFrame{
    private JList<String> out_list;
    public static DefaultListModel<String> listModel = new DefaultListModel<>();
	static float hrs;
	static JTextField addr;
	static JTextField vl;
	
	  private static SecretKeySpec secretKey;
	  private static byte[] key;
	  public static final String AES_Key = "Whatever the four letters it can be";

	  public static void setKey(final String myKey) {
	    MessageDigest sha = null;
	    try {
	      key = myKey.getBytes("UTF-8");
	      sha = MessageDigest.getInstance("SHA-1");
	      key = sha.digest(key);
	      key = Arrays.copyOf(key, 16);
	      secretKey = new SecretKeySpec(key, "AES");
	    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
	      e.printStackTrace();
	    }
	  }

	  public static String encrypt(final String strToEncrypt, final String secret) {
	    try {
	      setKey(secret);
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	      return Base64.getEncoder()
	        .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	    } catch (Exception e) {
	      System.out.println("Error while encrypting: " + e.toString());
	    }
	    return null;
	  }

	  public static String decrypt(final String strToDecrypt, final String secret) {
	    try {
	      setKey(secret);
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	      cipher.init(Cipher.DECRYPT_MODE, secretKey);
	      return new String(cipher.doFinal(Base64.getDecoder()
	        .decode(strToDecrypt)));
	    } catch (Exception e) {
	      System.out.println("Error while decrypting: " + e.toString());
	    }
	    return null;
	  }
	
	class show_wrk extends JFrame{
		 
	    private Container c;
	 
	    public show_wrk(String pr_k){
	        setTitle("Worker:" + pr_k);
	        setBounds(300, 90, 1250, 280);
	        setResizable(false);
	 
	        c = getContentPane();
	        c.setLayout(null);
			   Connection con = null;
			   Statement stmt = null;
			   try {
			      Class.forName("org.sqlite.JDBC");
			      con = DriverManager.getConnection("jdbc:sqlite:audit.db");
			      con.setAutoCommit(false);

			      stmt = con.createStatement();
			      ResultSet rs = stmt.executeQuery( "SELECT * FROM WORKER WHERE Key='" + pr_k + "';" );
			      
			      while ( rs.next() ) {
			    	  	String name = rs.getString("Name");
			         	String id = rs.getString("ID");
			         	int month  = rs.getInt("month");
			         	int day  = rs.getInt("day");
			         	int year  = rs.getInt("year");
			         	String num = rs.getString("Work_number");
			         	String sal = rs.getString("Salary");
				        JLabel name_1 = new JLabel("Name:" + name);
				        name_1.setFont(new Font("Arial", Font.PLAIN, 20));
				        name_1.setSize(1500, 20);
				        name_1.setLocation(12, 20);
				        c.add(name_1);
				        
				        JLabel id_1 = new JLabel("ID:" + MainClass.decrypt(id, AES_Key));
				        id_1.setFont(new Font("Arial", Font.PLAIN, 20));
				        id_1.setSize(1500, 20);
				        id_1.setLocation(12, 50);
				        c.add(id_1);
				        
				        JLabel dob_1 = new JLabel("Date of Birth:" + + month + "/" + day + "/" + year);
				        dob_1.setFont(new Font("Arial", Font.PLAIN, 20));
				        dob_1.setSize(1500, 20);
				        dob_1.setLocation(12, 80);
				        c.add(dob_1);
				        
				        JLabel wn_1 = new JLabel("Work number:" + num);
				        wn_1.setFont(new Font("Arial", Font.PLAIN, 20));
				        wn_1.setSize(1500, 20);
				        wn_1.setLocation(12, 110);
				        c.add(wn_1);
				        
				        JLabel ctg_1 = new JLabel("Category:" + get_category(sal));
				        ctg_1.setFont(new Font("Arial", Font.PLAIN, 20));
				        ctg_1.setSize(1500, 20);
				        ctg_1.setLocation(12, 140);
				        c.add(ctg_1);
				        
				        JLabel sl_1 = new JLabel("Salary:$" + get_salary(sal) + " per hour");
				        sl_1.setFont(new Font("Arial", Font.PLAIN, 20));
				        sl_1.setSize(1500, 20);
				        sl_1.setLocation(12, 170);
				        c.add(sl_1);
				        
				    	float s = 0;
				    	hrs = 0;
				    	s = calc_sal_in_all_chains(pr_k);
				    	String sl = String.format("%.3g%n", s);
				        JLabel tae_1 = new JLabel("Total amount earned:$" + sl);
				        tae_1.setFont(new Font("Arial", Font.PLAIN, 20));
				        tae_1.setSize(1500, 20);
				        tae_1.setLocation(12, 200);
				        c.add(tae_1);
				        Color frgr = new Color(238, 238, 238);
				        Color bclr = new Color(12, 108, 88);
				        name_1.setForeground(frgr);
				        id_1.setForeground(frgr);
				        dob_1.setForeground(frgr);
				        wn_1.setForeground(frgr);
				        ctg_1.setForeground(frgr);
				        sl_1.setForeground(frgr);
				        tae_1.setForeground(frgr);
				        c.setBackground(bclr);
			      }
			      rs.close();
			      stmt.close();
			      con.close();
			   } catch ( Exception e ) {
			      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			      System.exit(0);
			   }

	        setVisible(true);
	    }
	}
	
	public MainClass() {
        out_list = new JList<>(listModel);
	    JMenuBar mb;
	    JMenu cr, add_el, disp, blck, clcl, mdf;
	    JMenuItem create_tbls, addwrk, addsal, addjob, dspwrk, dspsal, dspjob, disp_w, disp_daw, hblock, down, vrf, sl, csl, setjf, insj, set_sal, set_wn;
        mb = new JMenuBar();
        cr = new JMenu("Create");
        create_tbls = new JMenuItem("Create required tables");
        cr.add(create_tbls);
        mb.add(cr);
        cr.add(create_tbls);
        
        add_el = new JMenu("Add");
	    addwrk = new JMenuItem("Add record to the WORKER table");
	    addsal = new JMenuItem("Add record to the SALARY table");
	    addjob = new JMenuItem("Add record to the JOB table");
        add_el.add(addwrk);
        add_el.add(addsal);
        add_el.add(addjob);
        mb.add(add_el);

        disp = new JMenu("Display");
	    dspwrk = new JMenuItem("Display all records in the WORKER table");
	    dspsal = new JMenuItem("Display all records in the SALARY table");
	    dspjob = new JMenuItem("Display all records in the JOB table");
	    disp_w = new JMenuItem("Display all workers with their category");
	    disp_daw = new JMenuItem("Display all data about worker in the form");
        disp.add(dspwrk);
        disp.add(dspsal);
        disp.add(dspjob);
        disp.add(disp_w);
        disp.add(disp_daw);
        mb.add(disp);
        
        blck = new JMenu("Block");
	    hblock = new JMenuItem("Hash block");
	    down = new JMenuItem("Go down the chain");
	    vrf = new JMenuItem("Verify the integrity of a block");
        blck.add(hblock);
        blck.add(down);
        blck.add(vrf);
        mb.add(blck);
        
        clcl = new JMenu("Calculate");
        sl = new JMenuItem("Calculate the salary for the worker in a single chain");
        csl = new JMenuItem("Calculate the salary for the worker in all chains");
        clcl.add(sl);
        clcl.add(csl);
        mb.add(clcl);
        
        mdf = new JMenu("Modify");
        setjf = new JMenuItem("Set the value for the Job field to the WORKER record");
        insj = new JMenuItem("Insert a pointer to the record with job into the WORKER record");
        set_sal = new JMenuItem("Set the value for the Salary field to the WORKER record");
        set_wn = new JMenuItem("Set the new work number for the worker");
        mdf.add(setjf);
        mdf.add(insj);
        mdf.add(set_sal);
        mdf.add(set_wn);
        JLabel empty_l1 = new JLabel(" ");
        mdf.add(empty_l1);
        JLabel ad = new JLabel("Address:");
        mdf.add(ad);
        
        addr = new JTextField();
        mdf.add(addr);
        
        JLabel v = new JLabel("Value:");
        mdf.add(v);
        
        vl = new JTextField();
        mdf.add(vl);
        mb.add(mdf);
        setJMenuBar(mb);  
        
        JButton extr = new JButton("Extract");
        mb.add(extr);
        
        JTextField for_e_v = new JTextField();
        mb.add(for_e_v);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(out_list);
        out_list.setLayoutOrientation(JList.VERTICAL);
        Color frgr = new Color(238, 238, 238);
        Color bclr = new Color(10, 10, 14);
        Color bl = new Color(0, 188, 243);
        out_list.setForeground(frgr);
        out_list.setBackground(bclr);
		mb.setBackground(bl);
		cr.setForeground(frgr);
		add_el.setForeground(frgr);
		disp.setForeground(frgr);
		blck.setForeground(frgr);
		clcl.setForeground(frgr);
		mdf.setForeground(frgr);
		addwrk.setBackground(bl);
		addsal.setBackground(bl);
		addjob.setBackground(bl);
		addwrk.setForeground(frgr);
		addsal.setForeground(frgr);
		addjob.setForeground(frgr);
	    setSize(1500,1010); 
	    add(scrollPane);
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setVisible(true);
	    setLayout(null);
	    
	    create_tbls.addActionListener(e ->
	    {
	    	create_worker_table();
	    	create_salary_table();
	    	create_job_table();
	    });
	    addwrk.addActionListener(e ->
	    {
	    	add_rec_wrkr();
	    });
	    addsal.addActionListener(e ->
	    {
		    add_rec_to_salary();
	    });
	    addjob.addActionListener(e ->
	    {
	    	add_rec_job();
	    });
	    dspwrk.addActionListener(e ->
	    {
	    	disp_worker();
	    });
	    dspsal.addActionListener(e ->
	    {
		    disp_sal();
	    });
	    dspjob.addActionListener(e ->
	    {
	    	disp_job();
	    });
	    hblock.addActionListener(e ->
	    {
	    	add_to_list("Block hash: "+hash_block(JOptionPane.showInputDialog("Enter the address of the block to hash")));
	    });
	    vrf.addActionListener(e ->
	    {
	    	String addrs = JOptionPane.showInputDialog("Enter the address of the block to hash");
	    	String cbhash = hash_block(addrs);
	    	String fr_k = get_f_key(addrs);
	    	if (fr_k.equals("0")) {
	    		JFrame er = new JFrame("Swing Tester");
	    		JOptionPane.showMessageDialog(er, "Can't verify the integrity of the last block in the chain","Block Verification Error!", JOptionPane.ERROR_MESSAGE);
	    	}
	    	else {
		    	String sthash = get_hash(fr_k);
		        if(cbhash.equals(sthash))
		            add_to_list("The integrity of the block " + addrs + " is verified!");
		        else
		        	add_to_list("Integrity verification of the block " + addrs + " has failed!");
	    	}
	    });
	    disp_w.addActionListener(e ->
	    {
	    	disp_worker_sal();
	    });
	    disp_daw.addActionListener(e ->
	    {
	    	String pr_k = JOptionPane.showInputDialog("Enter the key of the worker");
	    	show_wrk f = new show_wrk(pr_k);
	    });
	    down.addActionListener(e ->
	    {
	    	add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Elements down the chain/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
	    	go_down_the_chain(JOptionPane.showInputDialog("Enter the address of the first block in the chain"));
	    	add_to_list("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////THE END OF THE CHAIN////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
	    	add_to_list(" ");
	    	add_to_list(" ");
	    });
	    sl.addActionListener(e ->
	    {
	    	String pr_k = JOptionPane.showInputDialog("Enter the key of the worker");
	    	float s = 0;
	    	hrs = 0;
	    	s = calc_sal(pr_k);
	    	add_to_list("Worker");
	    	disp_worker_name_id(pr_k);
	    	add_to_list("Have earned $" + s);
	    });
	    csl.addActionListener(e ->
	    {
	    	String pr_k = JOptionPane.showInputDialog("Enter the key of the worker");
	    	float s = 0;
	    	hrs = 0;
	    	s = calc_sal_in_all_chains(pr_k);
	    	add_to_list("Worker");
	    	disp_worker_name_id(pr_k);
	    	add_to_list("Have earned $" + s);
	    });
	    extr.addActionListener(e ->
	    {
	    	for_e_v.setText((String) out_list.getSelectedValue());
	    });
	    setjf.addActionListener(e ->
	    {
	    	set_job();
	    });
	    insj.addActionListener(e ->
	    {
	    	insert_job();
	    });
	    set_sal.addActionListener(e ->
	    {
	    	set_sal();
	    });
	    set_wn.addActionListener(e ->
	    {
	    	set_wn();
	    });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Database");       
        this.setSize(1500,1010);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
	
	public static void set_job() {
		String key = addr.getText();
		String n_job = vl.getText();
		insert_job_to_worker(n_job, key);
	}
	
	public static void insert_job() {
		String key = addr.getText();
		String job_pr = extr_job(key);
		String n_job = vl.getText();
		insert_job_to_worker(job_pr + "," + n_job, key);
	}
	
	public static String extr_job(String key) {
		   Connection c = null;
		   Statement stmt = null;
		   String jb_t_ret = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT Job FROM WORKER WHERE Key ='"+key+"';"  );
		      
		      while ( rs.next() ) {
		         String job = rs.getString("Job");
		         jb_t_ret = job;
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return jb_t_ret;
	}
	
	public static void insert_job_to_worker(String nv, String key) {
		   Connection c = null;
		   Statement stmt = null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      String sql = "UPDATE WORKER set Job = '"+nv+"' where Key='"+key+"';";
		      stmt.executeUpdate(sql);
		      c.commit();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		return;
	}
	
	public static void set_sal() {
		String key = addr.getText();
		String sl = vl.getText();
		   Connection c = null;
		   Statement stmt = null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      String sql = "UPDATE WORKER set Salary = '"+sl+"' where Key='"+key+"';";
		      stmt.executeUpdate(sql);
		      c.commit();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		return;
	}
	
	public static void set_wn() {
		String key = addr.getText();
		String wn = vl.getText();
		   Connection c = null;
		   Statement stmt = null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      String sql = "UPDATE WORKER set Work_number = '"+wn+"' where Key='"+key+"';";
		      stmt.executeUpdate(sql);
		      c.commit();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		return;
	}
    
    public static void add_to_list(String add) {
    	listModel.addElement(add);
    }
	
	public static String gen_rnd(int n) {
        StringBuilder str = new StringBuilder();
		SecureRandom number = new SecureRandom();
		for (int i = 0; i < n; i++) {
		 str.append((char)(65 + (number.nextInt(26))));
		}
		return str.toString();
	}
	
	public static byte[] obtainSHA(String s) throws NoSuchAlgorithmException {   
		MessageDigest msgDgst = MessageDigest.getInstance("SHA-512");  
		return msgDgst.digest(s.getBytes(StandardCharsets.UTF_8));  
	}  
	  
	public static String toHexStr(byte[] hash) {   
		BigInteger no = new BigInteger(1, hash);
		StringBuilder hexStr = new StringBuilder(no.toString(16));  
		while (hexStr.length() < 32)  
		{  
			hexStr.insert(0, '0');  
		}  
		return hexStr.toString();  
	}  
	
	public static void create_worker_table() {
	      Connection c = null;
	      Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:audit.db");
	         add_to_list("Opened database successfully");

	         stmt = c.createStatement();
	         String sql = "CREATE TABLE WORKER" +
                     " (Key           TEXT      NOT NULL, " + 
                     " Name	          TEXT      NOT NULL, " + 
                     " ID	          TEXT      NOT NULL, " + 
                     " Month          INT       NOT NULL, " + 
                     " Day            INT       NOT NULL, " + 
                     " Year           INT       NOT NULL, " + 
                     " Work_number    TEXT      NOT NULL, " + 
                     " Salary         TEXT      NOT NULL, " + 
                     " Job            TEXT      NOT NULL)"; 
	         stmt.executeUpdate(sql);
	         stmt.close();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      add_to_list("Table called WORKER created successfully!");
	      return;
	}
	
	public static void create_salary_table() {
	      Connection c = null;
	      Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:audit.db");
	         add_to_list("Opened database successfully");

	         stmt = c.createStatement();
	         String sql = "CREATE TABLE SALARY" +
                   " (Primary_key   TEXT    NOT NULL, " + 
                   " Category       INT     NOT NULL, " + 
                   " Salary         FLOAT   NOT NULL)"; 
	         stmt.executeUpdate(sql);
	         stmt.close();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      add_to_list("Table called SALARY created successfully!");
	      return;
	}
	
	public static void create_job_table() {
	      Connection c = null;
	      Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:audit.db");
	         add_to_list("Opened database successfully!");

	         stmt = c.createStatement();
	         String sql = "CREATE TABLE JOB" +
                 " (Primary_key   TEXT    NOT NULL, " +
                 " Prev_block     TEXT    NOT NULL, " +
                 " Prev_hash      TEXT    NOT NULL, " + 
                 " Org_name       TEXT    NOT NULL, " + 
                 " Date           TEXT    NOT NULL, " + 
                 " Hours          FLOAT   NOT NULL, " + 
                 " Foreign_key    TEXT    NOT NULL)"; 
	         stmt.executeUpdate(sql);
	         stmt.close();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      add_to_list("Table called JOB created successfully");
	      return;
	}
	
	public static void add_record_to_worker(String name, String id, int month, int day, int year, String num, String sal, String work) {
	      Connection c = null;
	      Statement stmt = null;
	      String key = gen_rnd(128);
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:audit.db");
	         c.setAutoCommit(false);

	         stmt = c.createStatement();
	         String sql = "INSERT INTO WORKER (Key,Name,ID,Month,Day,Year,Work_number,Salary,Job) " +
	                        "VALUES ('"+key+"', '"+name+"', '"+id+"', "+month+", "+day+", "+year+", '"+num+"', '"+sal+"', '"+work+"' );"; 
	         stmt.executeUpdate(sql);
	         stmt.close();
	         c.commit();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      add_to_list("Record created successfully");
	      return;
	}
	
	public static void add_rec_to_salary() {
	      Connection c = null;
	      Statement stmt = null;
	      String key = gen_rnd(128);
	      int cat = Integer.parseInt(JOptionPane.showInputDialog("Enter the category"));
		  float sal = Float.parseFloat(JOptionPane.showInputDialog("Enter the salary $/hour"));
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:audit.db");
	         c.setAutoCommit(false);

	         stmt = c.createStatement();
	         String sql = "INSERT INTO SALARY (Primary_key, Category, Salary) " +
	                        "VALUES ('"+key+"', "+cat+", "+sal+" );"; 
	         stmt.executeUpdate(sql);
	         stmt.close();
	         c.commit();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      add_to_list("Record created successfully");
	      return;
	}
	
	public static void add_record_to_job(String key, String prb, String prh, String org, String date, float h, String fk) {
	      Connection c = null;
	      Statement stmt = null;
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:audit.db");
	         c.setAutoCommit(false);
	         stmt = c.createStatement();
	         String sql = "INSERT INTO JOB (Primary_key,Prev_block,Prev_hash,Org_name,Date,Hours,Foreign_key) " +
	                        "VALUES ('"+key+"', '"+prb+"', '"+prh+"', '"+org+"', '"+date+"', "+h+", '"+fk+"' );"; 
	         stmt.executeUpdate(sql);
	         stmt.close();
	         c.commit();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      add_to_list("Record created successfully");
	      return;
	}
	
	public static void add_rec_wrkr() {
		String name = JOptionPane.showInputDialog("Enter the name of the worker");
		String id = JOptionPane.showInputDialog("Enter the worker's ID");
	    int m = Integer.parseInt(JOptionPane.showInputDialog("Enter the worker's month of birth"));
	    int d = Integer.parseInt(JOptionPane.showInputDialog("Enter the worker's day of birth"));
	    int y = Integer.parseInt(JOptionPane.showInputDialog("Enter the worker's year of birth"));
		String num = JOptionPane.showInputDialog("Enter the worker's work number");
		String sal = JOptionPane.showInputDialog("Paste the address of the record with salary");
		String job = JOptionPane.showInputDialog("Paste the address of the first block with the job records");
	    add_record_to_worker(name, MainClass.encrypt(id, AES_Key), m, d, y, num, sal, job);
	    return;
	}
	
	public static void add_rec_job() {
		String prb = JOptionPane.showInputDialog("Paste the address of the previous block\nIf this is the first block in the chain then enter '0'");
		String key = gen_rnd(128);
		String org = JOptionPane.showInputDialog("Enter the organization where the job was carried out");
		String date = JOptionPane.showInputDialog("Enter the date when the job was carried out");
		float h = Float.parseFloat(JOptionPane.showInputDialog("Enter the number of hours worked"));
		if (prb == "0") {
			add_record_to_job(key, "0", "0", org, date, h, "0");
		}
		else {
			set_fk(key, prb);
			String hash = hash_block(prb);
			add_record_to_job(key, prb, hash, org, date, h, "0");
		}
		return;
	}
	
	public static void set_fk(String fkey, String prkey) {
		   Connection c = null;
		   Statement stmt = null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      String sql = "UPDATE JOB set Foreign_key ='"+fkey+"' where Primary_key='"+prkey+"';";
		      stmt.executeUpdate(sql);
		      c.commit();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		return;
	}
	
	public static String hash_block(String pr_key) {
		   Connection c = null;
		   Statement stmt = null;
		   String hash = "";
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOB WHERE Primary_key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		         String prom_key = rs.getString("Primary_key");
		         String pr_block = rs.getString("Prev_block");
		         String pr_hash = rs.getString("Prev_hash");
		         String org_name = rs.getString("Org_name");
		         String date = rs.getString("Date");
		         float h  = rs.getFloat("Hours");
		         String f_key = rs.getString("Foreign_key");
		         hash = toHexStr(obtainSHA(prom_key+pr_block+pr_hash+org_name+date+h+f_key));
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   
		return hash;
	}
	
	public static void disp_worker() {
		   Connection c = null;
		   Statement stmt = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM WORKER;" );
		      add_to_list("/////////////////////Elements in the WORKER table: /////////////////////");
		      
		      while ( rs.next() ) {
		         String key = rs.getString("Key");
		         String name = rs.getString("Name");
		         String id = rs.getString("ID");
		         int month  = rs.getInt("month");
		         int day  = rs.getInt("day");
		         int year  = rs.getInt("year");
		         String num = rs.getString("Work_number");
		         String sal = rs.getString("Salary");
		         String job = rs.getString("Job");
		         
		         add_to_list( "Key: " + key);
		         add_to_list( "Name: " + name);
		         add_to_list( "ID: " + id);
		         add_to_list( "Month of Birth: " + month);
		         add_to_list( "Day of Birth: " + day);
		         add_to_list( "Year of Birth: " + year);
		         add_to_list( "Work number: " + num);
		         add_to_list( "Address of the table with salary: " + sal);
		         add_to_list( "Address of the table with job: " + job);
		         add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		         add_to_list(" ");
		      }
		      add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////THE END/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		      add_to_list(" ");
		      add_to_list(" ");
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static void disp_job() {
		   Connection c = null;
		   Statement stmt = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOB;" );
		      add_to_list("/////////////////////Elements in the JOB table: /////////////////////");
		      
		      while ( rs.next() ) {
		         String pr_key = rs.getString("Primary_Key");
		         String prev_block = rs.getString("Prev_block");
		         String prev_hash = rs.getString("Prev_hash");
		         String org_name = rs.getString("Org_name");
		         String date = rs.getString("Date");
		         float  h = rs.getFloat("Hours");
		         String f_key = rs.getString("Foreign_key");
		         add_to_list( "Primary key of the block: " + pr_key);
		         add_to_list( "Address of the previous block: " + prev_block);
		         add_to_list( "Hash of the previous block: " + prev_hash);
		         add_to_list( "Name of the organization: " + org_name);
		         add_to_list( "Date when the work was carried out is " + date);
		         add_to_list( "Number of hours worked is " + h);
		         add_to_list( "Foreign_key: " + f_key);
		         add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		         add_to_list(" ");
		      }
		      add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////THE END/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		      add_to_list(" ");
		      add_to_list(" ");
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static void disp_sal() {
		   Connection c = null;
		   Statement stmt = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM SALARY;" );
		      add_to_list("/////////////////////Elements in the SALARY table: /////////////////////");
		      
		      while ( rs.next() ) {
		         String pr_key = rs.getString("Primary_Key");
		         int ctg = rs.getInt("Category");
		         float  s = rs.getFloat("Salary");
		         add_to_list( "Primary key of the block: " + pr_key);
		         add_to_list( "Category of the worker: " + ctg);
		         add_to_list( "Salary $/hour: " + s);
		         add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		         add_to_list(" ");
		      }
		      add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////THE END/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		      add_to_list(" ");
		      add_to_list(" ");
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static void disp_worker_sal() {
		   Connection c = null;
		   Statement stmt = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM WORKER;" );
		      add_to_list("/////////////////////Elements in the WORKER table: /////////////////////");
		      
		      while ( rs.next() ) {
		         String name = rs.getString("Name");
		         String id = rs.getString("ID");
		         int month  = rs.getInt("month");
		         int day  = rs.getInt("day");
		         int year  = rs.getInt("year");
		         String num = rs.getString("Work_number");
		         String sal = rs.getString("Salary");
		         
		         add_to_list( "Name: " + name);
		         add_to_list( "ID: " + id);
		         add_to_list( "Date of Birth: " + month + "/" + day + "/" + year );
		         add_to_list( "Work number: " + num);
		         add_to_list( "Category: " + get_category(sal));
		         add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		         add_to_list(" ");
		      }
		      add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////THE END/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		      add_to_list(" ");
		      add_to_list(" ");
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static String get_category(String pr_key) {
		   Connection c = null;
		   Statement stmt = null;
		   String cat = "";
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM SALARY WHERE Primary_key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		         int ctg = rs.getInt("Category");
		         cat += ctg;
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   
		return cat;
	}
	
	public static float get_salary(String pr_key) {
		   Connection c = null;
		   Statement stmt = null;
		   float slr = 0;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM SALARY WHERE Primary_key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		         slr = rs.getFloat("Salary");
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   
		return slr;
	}
	
	public static String get_f_key(String pr_key) {
		   Connection c = null;
		   Statement stmt = null;
		   String fk = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOB WHERE Primary_key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		         fk = rs.getString("Foreign_key");
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   
		return fk;
	}
	
	public static String get_hash(String pr_key) {
		   Connection c = null;
		   Statement stmt = null;
		   String hash = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOB WHERE Primary_key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		         hash = rs.getString("Prev_hash");
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   
		return hash;
	}
	
	public static float calc_sal(String pr_key) {
		   Connection c = null;
		   Statement stmt = null;
		   float slr = 0;
		   String wrk = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM WORKER WHERE Key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		    	  slr = get_salary(rs.getString("Salary"));
		    	  String job = rs.getString("Job");
		    	  if(job.length() >128) {
		    		  String[] addrss = job.split(",");
		    		  Integer v = Integer.parseInt(JOptionPane.showInputDialog("Turns out there are " + addrss.length + " chains for that worker\n"
		    		  		+ "Enter the number from 0 to " + (addrss.length-1) + " to calculate the salary for the selected chain"));
		    		  wrk = addrss[v];
		    	  }
		    	  else {
		    	  wrk = job;
		    	  }
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   add_hours_down_the_chain(wrk);
		return slr * hrs;
	}
	
	public static float calc_sal_in_all_chains(String pr_key) {
		   int n = num_of_chains(pr_key);
		   float slr = 0;
		   for (int i = 0; i < n; i++) {
		   Connection c = null;
		   Statement stmt = null;
		   String wrk = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM WORKER WHERE Key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		    	  if (i == 0) {
		    		  slr = get_salary(rs.getString("Salary"));
		    	  }
		    	  String job = rs.getString("Job");
		    	  if(job.length() >128) {
		    		  String[] addrss = job.split(",");
		    		  wrk = addrss[i];
		    	  }
		    	  else {
		    	  wrk = job;
		    	  }
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   add_hours_down_the_chain(wrk);
		   }
		return slr * hrs;
	}
	
	public static int num_of_chains(String pr_key) {
		   Connection c = null;
		   Statement stmt = null;
		   int n = 0;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM WORKER WHERE Key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		    	  String job = rs.getString("Job");
		    		  String[] addrss = job.split(",");
		    		  n = addrss.length;
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		return n;
	}
	
	public static void add_hours_down_the_chain(String addr) {
		   Connection c = null;
		   Statement stmt = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOB WHERE Primary_key ='"+addr+"';");
		      
		      while ( rs.next() ) {
		         float  h = rs.getFloat("Hours");
		         hrs += h;
		         String f_key = rs.getString("Foreign_key");
			       if (f_key != "0") {
			    	   add_hours_down_the_chain(f_key);
			       }
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static void disp_worker_name_id(String pr_key) {
		   Connection c = null;
		   Statement stmt = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM WORKER WHERE Key ='"+pr_key+"';" );
		      while ( rs.next() ) {
		         String key = rs.getString("Key");
		         String name = rs.getString("Name");
		         
		         add_to_list(key);
		         add_to_list(name);
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static void go_down_the_chain(String addr) {
		   Connection c = null;
		   Statement stmt = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOB WHERE Primary_key ='"+addr+"';");
		      
		      while ( rs.next() ) {
		         String pr_key = rs.getString("Primary_Key");
		         String prev_block = rs.getString("Prev_block");
		         String prev_hash = rs.getString("Prev_hash");
		         String org_name = rs.getString("Org_name");
		         String date = rs.getString("Date");
		         float  h = rs.getFloat("Hours");
		         String f_key = rs.getString("Foreign_key");
		         add_to_list( "Primary key of the block: " + pr_key);
		         add_to_list( "Address of the previous block: " + prev_block);
		         add_to_list( "Hash of the previous block: " + prev_hash);
		         add_to_list( "Name of the organization: " + org_name);
		         add_to_list( "Date when the work was carried out is " + date);
		         add_to_list( "Number of hours worked is " + h);
		         add_to_list( "Foreign_key: " + f_key);
		         add_to_list("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		         add_to_list(" ");
			       if (f_key != "0") {
			    	   go_down_the_chain(f_key);
			       }
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void less_than() {
		   Connection c = null;
		   Statement stmt = null;
		   Scanner in = new Scanner(System.in) ; ////// Activate the scanner
		   float pr;
		   System.out.print("Enter the price: ");
		   pr = in.nextFloat();
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM TOYS;" );
		      add_to_list("/////////////////////Elements in the database: ");
		      
		      while ( rs.next() ) {
		         String  name = rs.getString("name");
		         int amount  = rs.getInt("amount");
		         float  price = rs.getFloat("price");
		         int minage = rs.getInt("minage");
		         if (price < pr) {		         
		         add_to_list( "NAME = " + name );
		         add_to_list( "AMOUNT = " + amount );
		         add_to_list( "PRICE = " + price );
		         add_to_list( "MINAGE = " + minage );
		         add_to_list(" ");
		         }
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static void less_than_to_file() {
		   Connection c = null;
		   Statement stmt = null;
		   Scanner in = new Scanner(System.in) ; ////// Activate the scanner
		   float pr;
		   System.out.print("Enter the price: ");
		   pr = in.nextFloat();
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM TOYS;" );
		      write_less_than( "Items with price less than "+pr+"\n\n");
		      while ( rs.next() ) {
		         String  name = rs.getString("name");
		         int amount  = rs.getInt("amount");
		         float  price = rs.getFloat("price");
		         int minage = rs.getInt("minage");
		         if (price < pr) {		         
			         write_less_than( "Item:\n");
			         write_less_than( "NAME = " + name + "\n");
			         write_less_than( "PRICE = " + price + "\n\n");
		         }
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static void create_file() {
		   Connection c = null;
		   Statement stmt = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM TOYS;" );
		      writef("/////////////////////Elements in the database:\n\n");
		      
		      while ( rs.next() ) {
		         String  name = rs.getString("name");
		         float  price = rs.getFloat("price");
		         writef( "Item:\n");
		         writef( "NAME = " + name + "\n");
		         writef( "PRICE = " + price + "\n\n");
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   return;
	}
	
	public static void remove() {
	      Connection c = null;
	      Statement stmt = null;
	      Scanner in = new Scanner(System.in) ; ////// Activate the scanner
	      String name;
	      name = in.nextLine();
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:audit.db");
	         c.setAutoCommit(false);

	         stmt = c.createStatement();
	         String sql = "DELETE from Worker where NAME='"+name+"';";
	         stmt.executeUpdate(sql);
	         c.commit();

	      c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      return;
	}
	
	public static void writef(String s) {
		try {
	      FileWriter myWriter = new FileWriter("output.txt",true);
	      myWriter.write(s);
	      myWriter.close();
		}
		catch (IOException e) {
		    add_to_list("Can't create a file for the output data");
		    System. exit(1);
		    }
		return;
	}
	
	public static void write_less_than(String s) {
		try {
	      FileWriter myWriter = new FileWriter("less_than.txt",true);
	      myWriter.write(s);
	      myWriter.close();
		}
		catch (IOException e) {
		    add_to_list("Can't create a file for the output data");
		    System. exit(1);
		    }
		return;
	}

	public static void edit_amount(String na) {
		   Connection c = null;
		   Statement stmt = null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
			   Scanner in = new Scanner(System.in) ;
			   int am;
			   System.out.print("Enter the amount: ");
			   am = in.nextInt();
		      String sql = "UPDATE TOYS set AMOUNT = "+am+" where NAME='"+na+"';";
		      stmt.executeUpdate(sql);
		      c.commit();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		return;
	}
	
	public static void edit_price(String na) {
		   Connection c = null;
		   Statement stmt = null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:audit.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
			   Scanner in = new Scanner(System.in) ;
			   int pr;
			   System.out.print("Enter the price: ");
			   pr = in.nextInt();
		      String sql = "UPDATE TOYS set PRICE = "+pr+" where NAME='"+na+"';";
		      stmt.executeUpdate(sql);
		      c.commit();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		return;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
   public static void main( String args[] ) {
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               new MainClass();
           }
       });
   }
}

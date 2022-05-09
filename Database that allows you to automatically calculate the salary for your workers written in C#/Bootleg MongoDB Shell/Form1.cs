using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MongoDB.Bson;
using MongoDB.Driver;
using System.Configuration;
using System.Data;
/*
Database that allows you to automatically calculate the salary for your workers
Utilizes MongoDB
You can find more information here https://github.com/Northstrix/Miscellaneous
Distributed under the MIT License
© Copyright Maxim Bortnikov 2022
 */
namespace Bootleg_MongoDB_Shell
{
    public partial class Form1 : Form
    {
        public static string connectionString;
        public static MongoClient client;
        public static IMongoDatabase database;
        public Form1()
        {
            InitializeComponent();
            listView1.View = View.Details;
            listView1.GridLines = false;
            listView1.FullRowSelect = true;
            ColumnHeader header1;
            header1 = new ColumnHeader();
            header1.Text = "Some kinda bootleg console";
            header1.TextAlign = HorizontalAlignment.Left;
            header1.Width = 4096;
            listView1.Columns.Add(header1);
        }

        private void connectToolStripMenuItem_Click(object sender, EventArgs e)
        {
            try
            {
                connectionString = "mongodb://localhost:27017";
                client = new MongoClient(connectionString);
                database = client.GetDatabase("Audit");
                ListViewItem itm;
                itm = new ListViewItem("Successfully connected to the database");
                listView1.Items.Add(itm);
            }
            catch(Exception ex)
            {
                ListViewItem itm;
                itm = new ListViewItem(ex.ToString());
            }


        }

        private void listAllToolStripMenuItem_Click(object sender, EventArgs e) // List all databases
        {
            var dbList = client.ListDatabases().ToList();
            ListViewItem itm;
            itm = new ListViewItem("Availaible databases:");
            listView1.Items.Add(itm);
            foreach (var db in dbList)
            {
                itm  = new ListViewItem((String)(db["name"]));
                listView1.Items.Add(itm);
            }
        }

        private String gen_rnd() // Generate id
        {
            Random rd = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 128; i++)
            {
                sb.Append(Convert.ToChar(rd.Next(32, 120)));
            }
            return sb.ToString();
        }

        private void addToolStripMenuItem1_Click(object sender, EventArgs e) // Add record into the Salary
        {
            Form2 f2 = new Form2();
            f2.ShowDialog();
            if (Form2.adrec == true) {
                var Collec = database.GetCollection<BsonDocument>("Salary");
                String id = gen_rnd();
                var documnt = new BsonDocument
                {
                    {"Key",id},
                    {"Category",int. Parse(Form2.SetValueForText1)},
                    {"Salary",float. Parse(Form2.SetValueForText2)}
                };
                Collec.InsertOneAsync(documnt);
                ListViewItem itm;
                itm = new ListViewItem("Record successfully inserted with id: " + id);
                listView1.Items.Add(itm);
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void showAllToolStripMenuItem1_Click(object sender, EventArgs e) //Show all records in the Salary table
        {
            dataGridView1.Rows.Clear();
            dataGridView1.Columns.Clear();
            dataGridView1.Refresh();
            var sal = database.GetCollection<BsonDocument>("Salary");
            var documents = sal.Find(new BsonDocument()).ToList();
            int i = 0;
            foreach (BsonDocument doc in documents)
            {
                i++;
            }
            int j = i;
            String[] ext_sl1 = new String[i];
            String[] ext_sl2 = new String[i];
            String[] ext_sl3 = new String[i];
            ListViewItem itm;
            itm = new ListViewItem("All records in the Salary table:");
            listView1.Items.Add(itm);
            itm = new ListViewItem("Key, Category, Salary.");
            listView1.Items.Add(itm);
            i = 0;
            foreach (BsonDocument doc in documents)
            {
                itm = new ListViewItem(doc["Key"].ToString() + "  |  " + doc["Category"].ToString() + "  |  " + doc["Salary"].ToString());
                listView1.Items.Add(itm);
                ext_sl1[i] = doc["Key"].ToString();
                ext_sl2[i] = doc["Category"].ToString();
                ext_sl3[i] = doc["Salary"].ToString();
                i++;
            }
            dataGridView1.Columns.Add("Text", "Key");
            dataGridView1.Columns[0].Width = 175;
            dataGridView1.Columns.Add("Value", "Category");
            dataGridView1.Columns.Add("Value", "Salary");

            for (i = 0; i < j; i++)
            {
                dataGridView1.Rows.Add(new object[] { ext_sl1[i], ext_sl2[i], String.Format("{0:0.##}", float.Parse(ext_sl3[i])) });
            }
        }

        private void addToolStripMenuItem2_Click(object sender, EventArgs e) //Add record into the Job
        {
            Form3 f3 = new Form3();
            f3.ShowDialog();
            if (Form3.adrec == true)
            {
                var Collec = database.GetCollection<BsonDocument>("Job");
                String id = gen_rnd();
                var documnt = new BsonDocument
                {
                    {"Key",id},
                    {"Title",Form3.SetValueForText1},
                    {"Hours",float. Parse(Form3.SetValueForText2)}
                };
                Collec.InsertOneAsync(documnt);
                ListViewItem itm;
                itm = new ListViewItem("Record successfully inserted with id: " + id);
                listView1.Items.Add(itm);
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void addToolStripMenuItem_Click(object sender, EventArgs e) //Add worker
        {
            Form4 f4 = new Form4();
            f4.ShowDialog();
            if (Form4.adrec == true)
            {
                var Collec = database.GetCollection<BsonDocument>("Worker");
                String id = gen_rnd();
                var documnt = new BsonDocument
                {
                    {"Key",id},
                    {"Name",Form4.SetValueForText1},
                    {"ID",Form4.SetValueForText2},
                    {"Phone number",Form4.SetValueForText3},
                    {"Salary",Form4.sel_cat},
                    {"Jobs",""}
                };
                Collec.InsertOneAsync(documnt);
                ListViewItem itm;
                itm = new ListViewItem("Record successfully inserted with id: " + id);
                listView1.Items.Add(itm);
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void showAllToolStripMenuItem2_Click(object sender, EventArgs e) //Show all jobs
        {
            dataGridView1.Rows.Clear();
            dataGridView1.Columns.Clear();
            dataGridView1.Refresh();
            var jb = database.GetCollection<BsonDocument>("Job");
            var documents = jb.Find(new BsonDocument()).ToList();
            int i = 0;
            foreach (BsonDocument doc in documents)
            {
                i++;
            }
            int j = i;
            String[] ext_sl1 = new String[i];
            String[] ext_sl2 = new String[i];
            String[] ext_sl3 = new String[i];
            ListViewItem itm;
            itm = new ListViewItem("All records in the Job table:");
            listView1.Items.Add(itm);
            itm = new ListViewItem("Key, Title, Hours.");
            listView1.Items.Add(itm);
            i = 0;
            foreach (BsonDocument doc in documents)
            {
                itm = new ListViewItem(doc["Key"].ToString() + "  |  " + doc["Title"].ToString() + "  |  " + doc["Hours"].ToString());
                listView1.Items.Add(itm);
                ext_sl1[i] = doc["Key"].ToString();
                ext_sl2[i] = doc["Title"].ToString();
                ext_sl3[i] = doc["Hours"].ToString();
                i++;
            }
            dataGridView1.Columns.Add("Text", "Key");
            dataGridView1.Columns[0].Width = 175;
            dataGridView1.Columns.Add("Value", "Title");
            dataGridView1.Columns.Add("Value", "Hours");

            for (i = 0; i < j; i++)
            {
                dataGridView1.Rows.Add(new object[] { ext_sl1[i], ext_sl2[i], String.Format("{0:0.##}", float.Parse(ext_sl3[i])) });
            }
        }

        private void showAllToolStripMenuItem_Click(object sender, EventArgs e) //Show all workers
        {
            dataGridView1.Rows.Clear();
            dataGridView1.Columns.Clear();
            dataGridView1.Refresh();
            var wrk = database.GetCollection<BsonDocument>("Worker");
            var documents = wrk.Find(new BsonDocument()).ToList();
            int i = 0;
            foreach (BsonDocument doc in documents)
            {
                i++;
            }
            int j = i;
            String[] ext_sl1 = new String[i];
            String[] ext_sl2 = new String[i];
            String[] ext_sl3 = new String[i];
            String[] ext_sl4 = new String[i];
            float[] ext_sl5 = new float[i];
            ListViewItem itm;
            itm = new ListViewItem("All records in the Worker table:");
            listView1.Items.Add(itm);
            itm = new ListViewItem("Key, Name, ID, Phone number, Salary.");
            listView1.Items.Add(itm);
            i = 0;
            foreach (BsonDocument doc in documents)
            {
                float wsl = get_salary((doc["Salary"].ToString()));
                itm = new ListViewItem(doc["Key"].ToString() + "  |  " + doc["Name"].ToString() + "  |  " + doc["ID"].ToString() + "  |  " + doc["Phone number"].ToString() + "  |  " + wsl.ToString());
                listView1.Items.Add(itm);
                ext_sl1[i] = doc["Key"].ToString();
                ext_sl2[i] = doc["Name"].ToString();
                ext_sl3[i] = doc["ID"].ToString();
                ext_sl4[i] = doc["Phone number"].ToString();
                ext_sl5[i] = wsl;
                i++;
            }
            dataGridView1.Columns.Add("Text", "Key");
            dataGridView1.Columns[0].Width = 175;
            dataGridView1.Columns.Add("Text", "Name");
            dataGridView1.Columns.Add("Value", "ID");
            dataGridView1.Columns.Add("Value", "Phone number");
            dataGridView1.Columns.Add("Value", "Salary");

            for (i = 0; i < j; i++)
            {
                dataGridView1.Rows.Add(new object[] { ext_sl1[i], ext_sl2[i], ext_sl3[i], ext_sl4[i], ext_sl5[i] });
            }
        }

        private float get_salary(String id)
        {
            var sl = database.GetCollection<BsonDocument>("Salary");
            var filter = Builders<BsonDocument>.Filter.Eq("Key", id);
            var doc = sl.Find(filter).FirstOrDefault();
            return float.Parse(doc["Salary"].ToString());
        }

        private float get_hours(String id)
        {
            var jb = database.GetCollection<BsonDocument>("Job");
            var filter = Builders<BsonDocument>.Filter.Eq("Key", id);
            var doc = jb.Find(filter).FirstOrDefault();
            return float.Parse(doc["Hours"].ToString());
        }

        private void deleteToolStripMenuItem1_Click(object sender, EventArgs e) // Delete from Salary
        {
            Form5 f5 = new Form5("Salary","Category");
            f5.ShowDialog();
            if (Form5.adrec == true)
            {
                var wrk = database.GetCollection<BsonDocument>("Worker");
                var documents = wrk.Find(new BsonDocument()).ToList();
                bool rem = true;
                foreach (BsonDocument doc in documents)
                {
                    if (doc["Salary"].ToString() == Form5.t_rem)
                        rem = false;
                }

                if (rem == true) {
                    var collection = database.GetCollection<BsonDocument>("Salary");
                    var Deleteone = collection.DeleteOneAsync(Builders<BsonDocument>.Filter.Eq("Key", Form5.t_rem));
                    ListViewItem itm;
                    itm = new ListViewItem("Record " + Form5.t_rem + " removed successfully");
                    listView1.Items.Add(itm);
                }
                else
                {
                    ListViewItem itm;
                    itm = new ListViewItem("Can't delete record " + Form5.t_rem + " because at least one worker has reference to it");
                    listView1.Items.Add(itm);
                }
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void deleteToolStripMenuItem_Click(object sender, EventArgs e) //Delete from Worker
        {
            Form5 f5 = new Form5("Worker", "Name");
            f5.ShowDialog();
            if (Form5.adrec == true)
            {
                var collection = database.GetCollection<BsonDocument>("Worker");

                //deleting single record
                var Deleteone = collection.DeleteOneAsync(Builders<BsonDocument>.Filter.Eq("Key", Form5.t_rem));
                ListViewItem itm;
                itm = new ListViewItem("Record " + Form5.t_rem + " removed successfully");
                listView1.Items.Add(itm);
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void deleteToolStripMenuItem2_Click(object sender, EventArgs e) // Delete from Job
        {
            Form5 f5 = new Form5("Job", "Title");
            f5.ShowDialog();
            if (Form5.adrec == true)
            {
                var wrk = database.GetCollection<BsonDocument>("Worker");
                var documents = wrk.Find(new BsonDocument()).ToList();
                bool rem = true;
                foreach (BsonDocument doc in documents)
                {
                    if (doc["Jobs"].ToString().Contains(Form5.t_rem) == true)
                        rem = false;
                }

                if (rem == true)
                {
                    var collection = database.GetCollection<BsonDocument>("Job");
                    var Deleteone = collection.DeleteOneAsync(Builders<BsonDocument>.Filter.Eq("Key", Form5.t_rem));
                    ListViewItem itm;
                    itm = new ListViewItem("Record " + Form5.t_rem + " removed successfully");
                    listView1.Items.Add(itm);
                }
                else
                {
                    ListViewItem itm;
                    itm = new ListViewItem("Can't delete record " + Form5.t_rem + " because at least one worker has reference to it");
                    listView1.Items.Add(itm);
                }
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void editToolStripMenuItem1_Click(object sender, EventArgs e) // Edit Salary
        {
            Form6 f6 = new Form6("Salary", "Category");
            f6.ShowDialog();
            if (Form6.adrec == true)
            {
                var sal = database.GetCollection<BsonDocument>("Salary");
                var documents = sal.Find(new BsonDocument()).ToList();
                int i = 0;
                foreach (BsonDocument doc in documents)
                {
                    i++;
                }
                i = 0;
                String ctg = "";
                String slr = "";
                String k = "";
                foreach (BsonDocument doc in documents)
                {
                    if (Form6.ch_rec == doc["Key"].ToString())
                    {
                        k = doc["Key"].ToString();
                        ctg = doc["Category"].ToString();
                        slr = doc["Salary"].ToString();
                    }
                    i++;
                }
                Form7 f7 = new Form7(ctg, String.Format("{0:0.##}", float.Parse(slr)));
                f7.ShowDialog();
                if (Form7.m == true)
                {
                    ctg = Form7.ct;
                    slr = Form7.sl;
                    var update = Builders<BsonDocument>.Update.Set("Category", int.Parse(ctg));
                    var filter = Builders<BsonDocument>.Filter.Eq("Key", k);
                    var options = new UpdateOptions { IsUpsert = true };
                    var collection = database.GetCollection<BsonDocument>("Salary");
                    collection.UpdateOne(filter, update, options);
                    update = Builders<BsonDocument>.Update.Set("Salary", float.Parse(slr));
                    filter = Builders<BsonDocument>.Filter.Eq("Key", k);
                    options = new UpdateOptions { IsUpsert = true };
                    collection = database.GetCollection<BsonDocument>("Salary");
                    collection.UpdateOne(filter, update, options);
                    ListViewItem itm;
                    itm = new ListViewItem("Record " + k + " successfully modified");
                    listView1.Items.Add(itm);
                }
                else
                {
                    ListViewItem itm;
                    itm = new ListViewItem("Operation was canceled by user");
                    listView1.Items.Add(itm);
                }
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void editToolStripMenuItem2_Click(object sender, EventArgs e) // Edit Job
        {
            Form6 f6 = new Form6("Job", "Title");
            f6.ShowDialog();
            if (Form6.adrec == true)
            {
                var jb = database.GetCollection<BsonDocument>("Job");
                var documents = jb.Find(new BsonDocument()).ToList();
                int i = 0;
                foreach (BsonDocument doc in documents)
                {
                    i++;
                }
                i = 0;
                String ttl = "";
                String hrs = "";
                String k = "";
                foreach (BsonDocument doc in documents)
                {
                    if (Form6.ch_rec == doc["Key"].ToString())
                    {
                        k = doc["Key"].ToString();
                        ttl = doc["Title"].ToString();
                        hrs = doc["Hours"].ToString();
                    }
                    i++;
                }
                Form8 f8 = new Form8(ttl, String.Format("{0:0.##}", float.Parse(hrs)));
                f8.ShowDialog();
                if (Form8.m == true)
                {
                    ttl = Form8.tl;
                    hrs = Form8.hr;
                    var update = Builders<BsonDocument>.Update.Set("Title", ttl);
                    var filter = Builders<BsonDocument>.Filter.Eq("Key", k);
                    var options = new UpdateOptions { IsUpsert = true };
                    var collection = database.GetCollection<BsonDocument>("Job");
                    collection.UpdateOne(filter, update, options);
                    update = Builders<BsonDocument>.Update.Set("Hours", float.Parse(hrs));
                    filter = Builders<BsonDocument>.Filter.Eq("Key", k);
                    options = new UpdateOptions { IsUpsert = true };
                    collection = database.GetCollection<BsonDocument>("Job");
                    collection.UpdateOne(filter, update, options);
                    ListViewItem itm;
                    itm = new ListViewItem("Record " + k + " successfully modified");
                    listView1.Items.Add(itm);
                }
                else
                {
                    ListViewItem itm;
                    itm = new ListViewItem("Operation was canceled by user");
                    listView1.Items.Add(itm);
                }
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void editRecordToolStripMenuItem_Click(object sender, EventArgs e) // Edit Worker
        {
            Form6 f6 = new Form6("Worker", "Name");
            f6.ShowDialog();
            if (Form6.adrec == true)
            {
                var wr = database.GetCollection<BsonDocument>("Worker");
                var documents = wr.Find(new BsonDocument()).ToList();
                int i = 0;
                foreach (BsonDocument doc in documents)
                {
                    i++;
                }
                i = 0;
                String k = "";
                String nm = "";
                String id = "";
                String pn = "";
                String sl = "";
                foreach (BsonDocument doc in documents)
                {
                    if (Form6.ch_rec == doc["Key"].ToString())
                    {
                        k = doc["Key"].ToString();
                        nm = doc["Name"].ToString();
                        id = doc["ID"].ToString();
                        pn = doc["Phone number"].ToString();
                        sl = doc["Salary"].ToString();
                    }
                    i++;
                }
                Form9 f9 = new Form9(k, nm, id, pn, sl);
                f9.ShowDialog();
                if (Form9.m == true)
                {
                    nm = Form9.nm;
                    id = Form9.id;
                    pn = Form9.pn;
                    sl = Form9.sl;
                    modify("Name", nm, k, "Worker");
                    modify("ID", id, k, "Worker");
                    modify("Phone number", pn, k, "Worker");
                    modify("Salary", sl, k, "Worker");
                    ListViewItem itm;
                    itm = new ListViewItem("Record " + k + " successfully modified");
                    listView1.Items.Add(itm);
                }
                else
                {
                    ListViewItem itm;
                    itm = new ListViewItem("Operation was canceled by user");
                    listView1.Items.Add(itm);
                }
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void modify(String field, String value, String key, String table)
        {
            var update = Builders<BsonDocument>.Update.Set(field, value);
            var filter = Builders<BsonDocument>.Filter.Eq("Key", key);
            var options = new UpdateOptions { IsUpsert = true };
            var collection = database.GetCollection<BsonDocument>(table);
            collection.UpdateOne(filter, update, options);
        }

        private void addJobToolStripMenuItem_Click(object sender, EventArgs e) // Add Job to worker
        {
            Form6 f6 = new Form6("Worker", "Name");
            f6.ShowDialog();
            if (Form6.adrec == true)
            {
                var wr = database.GetCollection<BsonDocument>("Worker");
                var documents = wr.Find(new BsonDocument()).ToList();
                int i = 0;
                foreach (BsonDocument doc in documents)
                {
                    i++;
                }
                i = 0;
                String jobs = "";
                foreach (BsonDocument doc in documents)
                {
                    if (Form6.ch_rec == doc["Key"].ToString())
                    {
                        jobs = doc["Jobs"].ToString();
                    }
                    i++;
                }
                Form10 f10 = new Form10();
                f10.ShowDialog();
                if (Form10.adrec == true)
                {
                    if (jobs == "")
                    {
                        modify("Jobs", Form10.n_job + "|", Form6.ch_rec, "Worker");
                        //ListViewItem itm1;
                        //itm1 = new ListViewItem("Empty");
                        //listView1.Items.Add(itm1);
                    }
                    else
                    {
                        modify("Jobs", jobs + Form10.n_job + "|", Form6.ch_rec, "Worker");
                        //ListViewItem itm1;
                        //itm1 = new ListViewItem("Not empty");
                        //listView1.Items.Add(itm1);
                    }
                    ListViewItem itm;
                    itm = new ListViewItem("Record " + Form6.ch_rec + " successfully modified");
                    listView1.Items.Add(itm);
                }
                else
                {
                    ListViewItem itm;
                    itm = new ListViewItem("Operation was canceled by user");
                    listView1.Items.Add(itm);
                }
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void removeJobToolStripMenuItem_Click(object sender, EventArgs e) // Remove Job from Worker
        {
            Form6 f6 = new Form6("Worker", "Name");
            f6.ShowDialog();
            if (Form6.adrec == true)
            {
                var wr = database.GetCollection<BsonDocument>("Worker");
                var documents = wr.Find(new BsonDocument()).ToList();
                int i = 0;
                foreach (BsonDocument doc in documents)
                {
                    i++;
                }
                i = 0;
                String jobs = "";
                foreach (BsonDocument doc in documents)
                {
                    if (Form6.ch_rec == doc["Key"].ToString())
                    {
                        jobs = doc["Jobs"].ToString();
                    }
                    i++;
                }
                Form11 f11 = new Form11(jobs);
                f11.ShowDialog();
                if (Form11.m == true)
                {
                    modify("Jobs", Form11.jbs, Form6.ch_rec, "Worker");
                    ListViewItem itm;
                    itm = new ListViewItem("Record " + Form6.ch_rec + " successfully modified");
                    listView1.Items.Add(itm);
                }
                else
                {
                    ListViewItem itm;
                    itm = new ListViewItem("Operation was canceled by user");
                    listView1.Items.Add(itm);
                }
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }

        private void calculateSalaryToolStripMenuItem_Click(object sender, EventArgs e) // Calculate salary for the worker
        {
            Form12 f12 = new Form12("Worker", "Name");
            f12.ShowDialog();
            if (Form12.adrec == true)
            {
                var wr = database.GetCollection<BsonDocument>("Worker");
                var documents = wr.Find(new BsonDocument()).ToList();
                String jobs = "";
                String name = "";
                float slph = 0;
                foreach (BsonDocument doc in documents)
                {
                    if (Form12.ch_rec == doc["Key"].ToString())
                    {
                        name = doc["Name"].ToString();
                        jobs = doc["Jobs"].ToString();
                        slph = get_salary((doc["Salary"].ToString()));
                    }
                }
                String[] job = jobs.Split("|");
                if (job.Length > 1) {
                    float hrs = 0;
                    for (int i = 0; i < job.Length - 1; i++)
                    {
                        hrs += get_hours(job[i]);
                    }
                    //ListViewItem itm1;
                    //itm1 = new ListViewItem("Worker " + name + " have earned $" + String.Format("{0:0.##}", (hrs * slph)));
                    //listView1.Items.Add(itm1);
                    //MessageBox.Show("Worker " + name + " have earned $" + String.Format("{0:0.##}", (hrs * slph)), name + "'s salary");
                    Form13 f13 = new Form13("Worker " + name + " have earned $" + String.Format("{0:0.##}", (hrs * slph)), name + "'s salary");
                    f13.ShowDialog();

                }
                else
                {
                    ListViewItem itm1;
                    itm1 = new ListViewItem("Worker " + name + " haven't performed any jobs yet!");
                    listView1.Items.Add(itm1);
                }
            }
            else
            {
                ListViewItem itm;
                itm = new ListViewItem("Operation was canceled by user");
                listView1.Items.Add(itm);
            }
        }
    }
}
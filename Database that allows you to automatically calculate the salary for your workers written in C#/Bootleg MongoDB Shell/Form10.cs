using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using MongoDB.Bson;
using MongoDB.Driver;
using System.Configuration;

namespace Bootleg_MongoDB_Shell
{
    public partial class Form10 : Form
    {
        public static String n_job;
        public static bool adrec = false;
        public static String[] ext_jbs;
        public Form10()
        {
            InitializeComponent();

            var jb = Form1.database.GetCollection<BsonDocument>("Job");
            var documents = jb.Find(new BsonDocument()).ToList();
            int i = 0;
            foreach (BsonDocument doc in documents)
            {
                i++;
            }
            ext_jbs = new String[i];
            i = 0;
            foreach (BsonDocument doc in documents)
            {
                ext_jbs[i] = doc["Key"].ToString();
                comboBox1.Items.Add(doc["Title"].ToString());
                i++;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            adrec = true;
            n_job = ext_jbs[comboBox1.SelectedIndex];
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            adrec = false;
            this.Close();
        }
    }
}

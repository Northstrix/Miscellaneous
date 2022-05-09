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
    public partial class Form11 : Form
    {
        public static String jbs;
        public static bool m = false;
        public static String[] job;
        public Form11(String jobs)
        {
            InitializeComponent();
            job = jobs.Split("|");
            for (int i = 0; i < job.Length - 1; i++) {
                comboBox1.Items.Add(get_title(job[i]));
            }
        }

        private String get_title(String id)
        {
            var jb = Form1.database.GetCollection<BsonDocument>("Job");
            var filter = Builders<BsonDocument>.Filter.Eq("Key", id);
            var doc = jb.Find(filter).FirstOrDefault();
            return doc["Title"].ToString();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            m = true;
            jbs = "";
            for (int i = 0; i < job.Length - 1; i++)
            {
                if (comboBox1.SelectedIndex != i)
                {
                    jbs += job[i] + "|";
                }
            }
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            m = false;
            this.Close();
        }
    }
}

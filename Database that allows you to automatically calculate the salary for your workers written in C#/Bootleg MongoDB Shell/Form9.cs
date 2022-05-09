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
    public partial class Form9 : Form
    {
        public static String nm;
        public static String id;
        public static String pn;
        public static String sl;
        public static bool m = false;
        public static String[] ext_sl1;
        public Form9(String key, String name, String id, String phone, String salary)
        {
            InitializeComponent();
            textBox1.Text = name;
            textBox2.Text = id;
            textBox3.Text = phone;
            var sal = Form1.database.GetCollection<BsonDocument>("Salary");
            var documents = sal.Find(new BsonDocument()).ToList();
            int i = 0;
            foreach (BsonDocument doc in documents)
            {
                i++;
            }
            ext_sl1 = new String[i];
            i = 0;
            foreach (BsonDocument doc in documents)
            {
                ext_sl1[i] = doc["Key"].ToString();
                comboBox1.Items.Add(doc["Category"].ToString());
                if (doc["Key"].ToString() == salary)
                    comboBox1.SelectedItem = doc["Category"].ToString();
                i++;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            nm = textBox1.Text;
            id = textBox2.Text;
            pn = textBox3.Text;
            sl = ext_sl1[comboBox1.SelectedIndex];
            m = true;
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            m = false;
            this.Close();
        }
    }
}

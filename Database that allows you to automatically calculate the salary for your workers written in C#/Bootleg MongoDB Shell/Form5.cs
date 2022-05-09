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
    public partial class Form5 : Form
    {
        public static String t_rem;
        public static bool adrec = false;
        public static String[] ext_sl1;
        public Form5(String table, String disp)
        {
            InitializeComponent();
            var sal = Form1.database.GetCollection<BsonDocument>(table);
            var documents = sal.Find(new BsonDocument()).ToList();
            int i = 0;
            foreach (BsonDocument doc in documents)
            {
                i++;
            }
            int j = i;
            ext_sl1 = new String[i];
            i = 0;
            foreach (BsonDocument doc in documents)
            {
                ext_sl1[i] = doc["Key"].ToString();
                comboBox1.Items.Add(doc[disp].ToString());
                i++;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            adrec = true;
            t_rem = ext_sl1[comboBox1.SelectedIndex];
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            adrec = false;
            this.Close();
        }
    }
}

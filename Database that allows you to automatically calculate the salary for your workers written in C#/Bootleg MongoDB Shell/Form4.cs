﻿using System;
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
    public partial class Form4 : Form
    {
        public static string SetValueForText1 = "";
        public static string SetValueForText2 = "";
        public static string SetValueForText3 = "";
        public static String sel_cat;
        public static bool adrec = false;
        public static String[] ext_sl1;
        public Form4()
        {
            InitializeComponent();

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
                i++;
            }
        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void label4_Click(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            SetValueForText1 = textBox1.Text;
            SetValueForText2 = textBox2.Text;
            SetValueForText3 = textBox3.Text;
            adrec = true;
            sel_cat = ext_sl1[comboBox1.SelectedIndex];
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            adrec = false;
            this.Close();
        }
    }
}

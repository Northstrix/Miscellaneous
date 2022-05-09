using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Bootleg_MongoDB_Shell
{
    public partial class Form3 : Form
    {
        public static string SetValueForText1 = "";
        public static string SetValueForText2 = "";
        public static bool adrec = false;
        public Form3()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            SetValueForText1 = textBox1.Text;
            SetValueForText2 = textBox2.Text;
            adrec = true;
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            adrec = false;
            this.Close();
        }
    }
}

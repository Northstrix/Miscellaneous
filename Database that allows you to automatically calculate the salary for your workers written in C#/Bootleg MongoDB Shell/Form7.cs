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
    public partial class Form7 : Form
    {
        public static String ct;
        public static String sl;
        public static bool m = false;
        public Form7(String ct, String sl)
        {
            InitializeComponent();
            textBox1.Text = ct;
            textBox2.Text = sl;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            m = true;
            ct = textBox1.Text;
            sl = textBox2.Text;
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            m = false;
            this.Close();
        }
    }
}

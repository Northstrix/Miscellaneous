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
    public partial class Form8 : Form
    {
        public static String tl;
        public static String hr;
        public static bool m = false;
        public Form8(String ttl, String hrs)
        {
            InitializeComponent();
            textBox1.Text = ttl;
            textBox2.Text = hrs;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            m = true;
            tl = textBox1.Text;
            hr = textBox2.Text;
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            m = false;
            this.Close();
        }
    }
}

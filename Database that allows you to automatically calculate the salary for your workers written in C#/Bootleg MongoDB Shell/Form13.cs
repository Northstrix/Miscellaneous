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
    public partial class Form13 : Form
    {
        public Form13(String content, String title)
        {
            InitializeComponent();
            label1.Text = content;
            this.Text = title;
            button1.Location = new Point()
            {
                X = panel1.Width / 2 - button1.Width / 2,
                Y = panel1.Height / 2 - button1.Height / 2
            };
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void Form13_Resize(object sender, EventArgs e)
        {
            button1.Location = new Point()
            {
                X = panel1.Width / 2 - button1.Width / 2,
                Y = panel1.Height / 2 - button1.Height / 2
            };
        }
    }
}

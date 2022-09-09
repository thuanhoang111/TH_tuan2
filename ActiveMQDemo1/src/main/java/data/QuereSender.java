package data;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.
import data.Person;
import helper.XMLConvert;

public class QuereSender extends JFrame implements ActionListener {
	public QuereSender() {
		JFrame frame = new JFrame("Chat Frame");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(400, 400);

		JMenuBar mb = new JMenuBar();

		JMenu m1 = new JMenu("FILE");

		JMenu m2 = new JMenu("Help");

		mb.add(m1);

		mb.add(m2);

		JMenuItem m11 = new JMenuItem("Open");

		JMenuItem m22 = new JMenuItem("Save as");

		m1.add(m11);

		m1.add(m22);

		// Creating the panel at bottom and adding components

		JPanel panel = new JPanel(); // the panel is not visible in output

		JLabel label = new JLabel("Enter Text");

		JTextField tf = new JTextField(10); // accepts upto 10 characters

		JButton send = new JButton("Send");

		JButton reset = new JButton("Reset");

		panel.add(label); // Components Added using Flow Layout

		panel.add(tf);

		panel.add(send);

		panel.add(reset);

		// Text Area at the Center

		JTextArea ta = new JTextArea();

		// Adding Components to the frame.

		frame.getContentPane().add(BorderLayout.SOUTH, panel);

//		frame.getContentPane().add(BorderLayout.NORTH, mb);

		frame.getContentPane().add(BorderLayout.CENTER, ta);

		frame.setVisible(true); 
	}
	public static void main(String[] args) throws Exception {
		new QuereSender().setVisible(false);
//config environment for JMS
		BasicConfigurator.configure();
//config environment for JNDI
		Properties settings = new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
//create context
		Context ctx = new InitialContext(settings);
//lookup JMS connection factory
		ConnectionFactory factory =

				(ConnectionFactory) ctx.lookup("ConnectionFactory");
//lookup destination. (If not exist-->ActiveMQ create once)
		Destination destination =

				(Destination) ctx.lookup("dynamicQueues/thanthidet");

//get connection using credential
		Connection con = factory.createConnection("admin", "admin");
//connect to MOM
		con.start();
//create session
		Session session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);
//create producer
		MessageProducer producer = session.createProducer(destination);
//create text message
		Message msg = session.createTextMessage("hello mesage from ActiveMQ");
		producer.send(msg);
		Person p = new Person(1001, "Thân Thị Đẹt", new Date());
		Person a = new Person(2002, "Hoang Hoa Thuan", new Date(2001, 12, 31));
		
		String xml = new XMLConvert<Person>(p).object2XML(p);
		
		msg = session.createTextMessage(xml);
		msg = session.createTextMessage(new XMLConvert<Person>(a).object2XML(a));
		producer.send(msg);
//		producer.send(msg)
		
//shutdown connection
		session.close();
		con.close();
		System.out.println("Finished...");
	}
//	@xe
	public void actionPerformed(ActionEvent e) {
//		 TODO Auto-generated method stub
		Object o = e.getSource();
//		if(o.equals(send))
	}
}
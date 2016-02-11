package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.Font;

import model.CardModel;
import model.CardStatus;
import model.ProxyModel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import action.FirstPageWP;
import action.NewFinalPageWP;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;

public class SBrowserSwing {

	private JFrame frmSbrowser;
	private WebDriver driver;
	FirefoxProfile profile;
	ArrayList<ProxyModel> proxyList;
	ArrayList<String> alList;
	ArrayList<String> agentList;
	// CountDownLatch openPageCountDown;
	CountDownLatch firstPageCountdown;//
	CountDownLatch executeCountdown;//
	boolean executeFinish;
	JTextArea tfCardInfo;
	static public String baseUrl="null";
	static public String targetUrl="null";
	CardModel currentCard;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public void initial() {
		File file = new File("profiles");
		profile = new FirefoxProfile(file);
		setProxy();
		generateAcceptLangList("acceptLanguage.txt");
		generateAgent("useragent.txt");
		setAcceptLang();
		setAgent();
	}

	public void setUp() throws Exception {
		initial();
		driver = new FirefoxDriver(profile);

		baseUrl = "null";
		driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	}

	public void generateProxyList(String filePath) {
		proxyList = new ArrayList<ProxyModel>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(filePath)));
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				ProxyModel proxyModel = new ProxyModel();
				proxyModel.generateProxyModel(tmp.trim());
				proxyList.add(proxyModel);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
	}

	public void generateAcceptLangList(String filepath) {
		alList = new ArrayList<String>();
		BufferedReader bReader;
		try {
			bReader = new BufferedReader(new FileReader(new File(
					"AcceptLanguage.txt")));
			String tmp = "";
			while ((tmp = bReader.readLine()) != null) {
				alList.add(tmp);
			}

			bReader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}

	public void generateAgent(String filepath) {
		agentList = new ArrayList<String>();
		BufferedReader bReader;
		try {
			bReader = new BufferedReader(new FileReader(new File(
					"useragent.txt")));
			String tmp = "";
			while ((tmp = bReader.readLine()) != null) {
				agentList.add(tmp);
			}

			bReader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}

	public void setAgent() {
		Random random = new Random();
		int i = random.nextInt(agentList.size());
		String agent = agentList.get(i);
		if (profile != null) {
			profile.setPreference("general.useragent.override", agent);

		}
	}

	public void setAcceptLang() {
		Random random = new Random();
		int i = random.nextInt(alList.size());
		String acceptLang = alList.get(i);
		if (profile != null) {
			profile.setPreference("Accept-Language", acceptLang);

		}
	}

	public void setProxy() {
		profile.setPreference("network.proxy.type", 1);
		profile.setPreference("network.proxy.socks", "127.0.0.1");
		profile.setPreference("network.proxy.socks_port", 9951);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SBrowserSwing window = new SBrowserSwing();
					window.frmSbrowser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SBrowserSwing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSbrowser = new JFrame();
		frmSbrowser.setTitle("SBrowser-WPEngine");
		frmSbrowser.setBounds(100, 100, 518, 438);
		frmSbrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSbrowser.getContentPane().setLayout(null);

		JButton button = new JButton("\u542F\u52A8\u6D4F\u89C8\u5668");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					setUp();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		button.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		button.setBounds(21, 31, 115, 37);
		frmSbrowser.getContentPane().add(button);

		JButton btncookies = new JButton("\u6E05\u9664Cookies");
		// ���Cookies
		btncookies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				driver.manage().deleteAllCookies();
			}
		});
		btncookies.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		btncookies.setBounds(21, 93, 115, 37);
		frmSbrowser.getContentPane().add(btncookies);
		JButton button_1 = new JButton("\u67E5\u770B\u4EE3\u7406");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				driver.get("http://www.check2ip.com");
			}
		});
		button_1.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		button_1.setBounds(21, 156, 115, 37);
		frmSbrowser.getContentPane().add(button_1);

		JScrollPane scr = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scr.setBounds(160, 31, 330, 170);
		tfCardInfo = new JTextArea();
		tfCardInfo.setLineWrap(true);
		tfCardInfo.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Card Info",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		tfCardInfo.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		tfCardInfo.setBounds(162, 19, 339, 186);
		scr.setViewportView(tfCardInfo);
		frmSbrowser.getContentPane().add(scr);

		JButton btSaveCardInfo = new JButton("\u4FDD\u5B58\u4FE1\u606F");
		btSaveCardInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO:
				if (currentCard == null)
					return;

				FileWriter fileWriter;
				try {
					fileWriter = new FileWriter("Result.txt", true);
					fileWriter.write("-----------------------------\r\n");
					fileWriter.write(currentCard.toString());
					fileWriter.write("baseUrl: "+ baseUrl);
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		btSaveCardInfo.setBounds(265, 369, 117, 29);
		frmSbrowser.getContentPane().add(btSaveCardInfo);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(21, 224, 480, 135);
		frmSbrowser.getContentPane().add(tabbedPane);

		JPanel wpPanel = new JPanel();
		tabbedPane.addTab("WP Engine", null, wpPanel, null);
		wpPanel.setLayout(null);

		JButton btnExcuteAd = new JButton("Open");
		btnExcuteAd.setBounds(20, 46, 73, 37);
		wpPanel.add(btnExcuteAd);
		btnExcuteAd.addActionListener(new ExuteWEAdAction());

		btnExcuteAd.setFont(new Font("΢���ź�", Font.PLAIN, 12));

		JButton btExcuteSecondPage = new JButton(
				"execute");
		btExcuteSecondPage.setBounds(152, 46, 73, 37);
		wpPanel.add(btExcuteSecondPage);
		btExcuteSecondPage.addActionListener(new ExcuteFinalPageAction());
		btExcuteSecondPage.setFont(new Font("΢���ź�", Font.PLAIN, 12));

		JButton btContinue = new JButton("continue");
		btContinue.setBounds(283, 46, 73, 37);
		wpPanel.add(btContinue);
		btContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (executeCountdown != null)
					executeCountdown.countDown();

				firstPageCountdown = new CountDownLatch(1);
				executeCountdown = new CountDownLatch(1);
			}
		});
		btContinue.setFont(new Font("΢���ź�", Font.PLAIN, 12));

		JComboBox cbWp = new JComboBox();
		cbWp.setBounds(120, 6, 236, 46);
		wpPanel.add(cbWp);
		cbWp.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (((String) e.getItem()).equals("wp7-thp-jill")) {
					baseUrl = "http://www.top-hosting-provider.com";
					targetUrl = "/id69.php";
				} else if (((String) e.getItem()).equals("wp8-tugnow-kathy")) {
					baseUrl = "http://www.webhosting.tugnow.com";
					targetUrl = "/go/wpengine.php";
				} else if (((String) e.getItem()).equals("wp3-superwebhostings-Shawn616")) {
					baseUrl = "http://www.superwebhostings.com";
					targetUrl = "/pid173.php";
				}

			}
		});
		cbWp.setModel(new DefaultComboBoxModel(new String[] { "wp7-thp-jill", "wp8-tugnow-kathy",
				"wp3-superwebhostings-Shawn616" }));
		cbWp.setSelectedIndex(0);

		JPanel mfPanel = new JPanel();
		tabbedPane.addTab("Mail Forwarding", null, mfPanel, null);
		mfPanel.setLayout(null);

		JComboBox cbGh = new JComboBox();
		cbGh.setModel(new DefaultComboBoxModel(new String[] {"MF1", "MF2"}));
		cbGh.setBounds(164, 6, 117, 27);
		mfPanel.add(cbGh);

		JButton btOpenGh = new JButton("\u6267\u884C\u7B2C\u4E00\u9875");
		btOpenGh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				driver.close();
			}
		});
		btOpenGh.setBounds(16, 45, 117, 38);
		mfPanel.add(btOpenGh);

		JButton btExcuteGh = new JButton("\u6267\u884C\u7B2C\u4E8C\u9875");
		btExcuteGh.setBounds(174, 45, 117, 38);
		mfPanel.add(btExcuteGh);

		JButton btContinueGH = new JButton("\u7EE7\u7EED");
		btContinueGH.setBounds(322, 45, 117, 38);
		mfPanel.add(btContinueGH);
		
		JButton button_2 = new JButton("\u9000\u51FA\u6D4F\u89C8\u5668");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		button_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_2.setBounds(21, 199, 115, 37);
		frmSbrowser.getContentPane().add(button_2);
	}

	class ExuteWEAdAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			firstPageCountdown = new CountDownLatch(1);
			executeCountdown = new CountDownLatch(1);

			ExecuteWPAd executewpAd = new ExecuteWPAd();
			executewpAd.start();

		}

	}

	class ExecuteWPAd extends Thread {
		@Override
		public void run() {
			List<CardModel> cardList = new ArrayList<CardModel>();
			readCardTxt("card.txt", cardList);
			for (CardModel card : cardList) {
				if (card.getcStatus().equals(CardStatus.Normal)
						|| card.getcStatus().equals(CardStatus.Unknown)) {
					currentCard = card;
					showCardInfo(card);
					FirstPageWP firstPage = new FirstPageWP(driver, card);
					NewFinalPageWP finalPage = new NewFinalPageWP(driver, card);
					try {

						firstPage.emuAction();
						firstPageCountdown.await();
						System.out.println("Second Page");
						finalPage.emuAction();
						executeFinish = true;
						executeCountdown.await();

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		}

		private void readCardTxt(String filePath, List<CardModel> cardList) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(new File(filePath)));
				String tmp = "";
				CardModel card;
				while ((tmp = br.readLine()) != null) {
					card = new CardModel();
					card.generateModel(tmp);
					cardList.add(card);
				}
			} catch (FileNotFoundException e) {
				System.out.println("No Card File!");
				e.printStackTrace();
			} catch (IOException e) {
			} finally {
				try {

					br.close();
				} catch (IOException e) {
				}
			}

		}

		public void showCardInfo(CardModel card) {
			tfCardInfo.setText(card.toString());
		}
	}

	// class FirstPageThread extends Thread
	// {
	// CardModel card;
	// public FirstPageThread(CardModel card)
	// {
	// this.card = card;
	// }
	//
	// @Override
	// public void run()
	// {
	// FirstPageWP firstPage = new FirstPageWP(driver, card);
	// try
	// {
	// firstPage.emuAction();
	//
	// firstPageCountdown.await();
	// System.out.println("Second Page");
	// //TODO������һ���ȴ�ʱ�ӣ�countdown���ȴ���һ����ť����ʱ����
	//
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	//
	// }
	// }

	class ExcuteFinalPageAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (firstPageCountdown != null)
				firstPageCountdown.countDown();

		}

	}
}

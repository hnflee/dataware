package cn.icesoft;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaSend {
	private static Logger logger = Logger.getLogger(MinaSend.class);
	private static int PORT = 58585;
	private static String HOST = "10.21.11.238";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 创建一个非阻塞的客户端程序
				IoConnector connector = new NioSocketConnector();
				// 设置链接超时时间
				connector.setConnectTimeout(30000);
				// 添加过滤器
				connector.getFilterChain().addLast(
						"codec",
						new ProtocolCodecFilter(new TextLineCodecFactory(Charset
								.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(),
								LineDelimiter.WINDOWS.getValue())));
				// 添加业务逻辑处理器类
				IcesoftClientHandler hadler=new IcesoftClientHandler();
				connector.setHandler(hadler);
				IoSession session = null;
				try {
					ConnectFuture future = connector.connect(new InetSocketAddress(
							HOST, PORT));// 创建连接
					future.awaitUninterruptibly();// 等待连接创建完成
					session = future.getSession();// 获得session
					UUID uuid_ = UUID.randomUUID();
					session.write("1010000000select t.tkt_nm, t.airline_3code, t.flight_nm, t.flight_date, t.flight_seg, t.cabin_code, t.market_fare, t.net_fare, t.agent_fee_rate, t.agent_fee, t.sp_fee_rate, t.sp_fee, t.insurance, t.agt_payment_amount, t.airport_tax, t.fuel_tax, t.booking_office_code, t.agent_name_cn, t.payment_no, t.order_no, t.pnr_nm, t.booking_date, t.issue_date, t.user_type, t.loginid, t.passenger_id, t.agt_payment_partner, t.product_code, t.source from et_prd.rt_et_sales_report t where t.airline_3code = '826' and t.airline_2code = 'GS' and t.trans_type = 'NORMAL' and t.issue_date between '2013-04-01' and '2013-05-14' and t.source in ('GQ', 'GC', 'GX', 'GT')\001"+uuid_.toString());// 发送消息
					
				} catch (Exception e) {
					logger.error("客户端链接异常...", e);
				}

				session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
				connector.dispose();
			}

}

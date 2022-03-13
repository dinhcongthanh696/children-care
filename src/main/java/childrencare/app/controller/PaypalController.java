package childrencare.app.controller;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import childrencare.app.configuration.PaypalPaymentIntent;
import childrencare.app.configuration.PaypalPaymentMethod;
import childrencare.app.service.PayService;
import childrencare.app.service.PaypalService;
import childrencare.app.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;


@Controller
public class PaypalController {
    @Autowired
    ReservationService reservationService;

    @Autowired
    PayService payService;

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private Logger log = LoggerFactory.getLogger(getClass());

    public static String getBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url =  new StringBuffer();
        url.append(scheme).append("://").append(serverName);
        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if(url.toString().endsWith("/")){
            url.append("/");
        }
        return url.toString();
    }

    @Autowired
    private PaypalService paypalService;


    @GetMapping("/pay")
    public String pay(HttpServletRequest request,
                      @RequestParam("price") double price,
                      @RequestParam("reservationId") Integer rid){
        String cancelUrl = this.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL + "?reservationId=" + rid;
        String successUrl = this.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS + "?reservationId=" + rid;
        try {
            Payment payment = paypalService.createPayment(
                    price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(@RequestParam("reservationId") Integer rid){
        return "redirect:/bookingSchedule?reservationId=" + rid;
    }

    @Transactional
    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("reservationId") Integer rid,
                             @RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId
                             ){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                payService.setPaymentMethod(2, rid);
                reservationService.changeStatusReservation(4, rid);
                return "thank_you";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}

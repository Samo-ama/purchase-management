package com.example.purchase.management.report;

import com.example.purchase.management.entity.Purchase;
import com.example.purchase.management.entity.Refund;
import org.springframework.stereotype.Component;
import java.util.List;
import java.time.format.DateTimeFormatter;

@Component
public class HtmlReportGenerator implements ReportGenerator {

    @Override
    public String generateReport(List<Purchase> purchases, List<Refund> refunds) {
        StringBuilder html = new StringBuilder();
        html.append("<html><header><h1 style='color: #000000; text-align: center; font-family: Arial, sans-serif;'>Transactions Report</h1></header>");
        html.append("<body style='font-family: Arial, sans-serif;'>");
        
        generatePurchasesTable(html, purchases);
        generateRefundsTable(html, refunds);
        
        html.append("</body></html>");
        return html.toString();
    }

    private void generatePurchasesTable(StringBuilder html, List<Purchase> purchases) {
        html.append("<h2 style='color: #006838;'>Purchases</h2>");
        html.append("<table style='border-collapse: collapse; width: 100%; margin-bottom: 20px;'>");
        html.append("<tr style='background-color: #006838; color: white;'>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>ID</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>First Name</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Last Name</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Phone</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Product</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Amount</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Created Date</th></tr>");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Purchase purchase : purchases) {
            html.append("<tr style='background-color: #f9f9f9;'>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(purchase.getId()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(purchase.getCustomer().getFirstName()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(purchase.getCustomer().getLastName()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(purchase.getCustomer().getPhone()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(purchase.getProduct().getName()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(purchase.getAmount()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(purchase.getDate().format(formatter)).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</table>");
    }

    private void generateRefundsTable(StringBuilder html, List<Refund> refunds) {
        html.append("<h2 style='color: #006838;'>Refunds</h2>");
        html.append("<table style='border-collapse: collapse; width: 100%;'>");
        html.append("<tr style='background-color: #006838; color: white;'>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>ID</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>First Name</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Last Name</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Phone</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Purchase ID</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Amount</th>");
        html.append("<th style='padding: 12px; border: 1px solid #ddd;'>Created Date</th></tr>");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Refund refund : refunds) {
            html.append("<tr style='background-color: #f9f9f9;'>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(refund.getId()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(refund.getCustomer().getFirstName()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(refund.getCustomer().getLastName()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(refund.getCustomer().getPhone()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(refund.getPurchase().getId()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(refund.getAmount()).append("</td>");
            html.append("<td style='padding: 12px; border: 1px solid #ddd;'>").append(refund.getDate().format(formatter)).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</table>");
    }
}
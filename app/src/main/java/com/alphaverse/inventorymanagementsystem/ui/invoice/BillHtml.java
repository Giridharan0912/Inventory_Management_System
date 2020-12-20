package com.alphaverse.inventorymanagementsystem.ui.invoice;

import com.alphaverse.inventorymanagementsystem.model.Invoice;
import com.alphaverse.inventorymanagementsystem.util.CurrentUserAPI;

import java.util.ArrayList;

/**
 * This class will generate bill with html in stringBuilder format and return in string format
 */
public class BillHtml {
    private final ArrayList<Invoice> invoices;

    public BillHtml(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

    public String bill() {
        CurrentUserAPI currentUserAPI = CurrentUserAPI.getInstance();
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title></title>\n" +
                "    \n" +
                "    <style>\n" +
                "    .invoice-box {\n" +
                "        max-width: 800px;\n" +
                "        margin: auto;\n" +
                "        padding: 30px;\n" +
                "        border: 1px solid #eee;\n" +
                "        box-shadow: 0 0 10px rgba(0, 0, 0, .15);\n" +
                "        font-size: 16px;\n" +
                "        line-height: 24px;\n" +
                "        font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;\n" +
                "        color: #555;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table {\n" +
                "        width: 100%;\n" +
                "        line-height: inherit;\n" +
                "        text-align: left;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table td {\n" +
                "        padding: 5px;\n" +
                "        vertical-align: top;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr td:nth-child(2) {\n" +
                "        text-align: right;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.top table td {\n" +
                "        padding-bottom: 20px;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.top table td.title {\n" +
                "        font-size: 45px;\n" +
                "        line-height: 30px;\n" +
                "        color: #333;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.information table td {\n" +
                "        padding-bottom: 40px;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.heading td {\n" +
                "        background: #eee;\n" +
                "        border-bottom: 1px solid #ddd;\n" +
                "        font-weight: bold;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.details td {\n" +
                "        padding-bottom: 20px;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.item td{\n" +
                "        border-bottom: 1px solid #eee;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.item.last td {\n" +
                "        border-bottom: none;\n" +
                "    }\n" +
                "    \n" +
                "    .invoice-box table tr.total td:nth-child(2) {\n" +
                "        border-top: 2px solid #eee;\n" +
                "        font-weight: bold;\n" +
                "    }\n" +
                "    \n" +
                "    @media only screen and (max-width: 600px) {\n" +
                "        .invoice-box table tr.top table td {\n" +
                "            width: 100%;\n" +
                "            display: block;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        \n" +
                "        .invoice-box table tr.information table td {\n" +
                "            width: 100%;\n" +
                "            display: block;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    /** RTL **/\n" +
                "    .rtl {\n" +
                "        direction: rtl;\n" +
                "        font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;\n" +
                "    }\n" +
                "    \n" +
                "    .rtl table {\n" +
                "        text-align: right;\n" +
                "    }\n" +
                "    \n" +
                "    .rtl table tr td:nth-child(2) {\n" +
                "        text-align: left;\n" +
                "    }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <div class=\"invoice-box\">\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "            <tr class=\"top\">\n" +
                "                <td colspan=\"2\">\n" +
                "                    <table>\n" +
                "                        <tr>\n" +
                "                            <td class=\"title\">\n" +
                "                               <a>" + currentUserAPI.getUserStoreName() + "<a>\n" +
                "                            </td>\n" +
                "                            \n" +
                "                            <td>\n" +
                "                                Date : 19:12:2020\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </td >\n" +
                "            </tr>\n" +
                "            \n" +
                "            <tr class=\"information\">\n" +
                "                <td colspan=\"2\">\n" +
                "                    <table>\n" +
                "                        <tr>\n" +
                "                            <td>\n" +
                "                                \n" +
                "                                12345 Sunny Road<br>\n" +
                "                                Sunnyville, CA 12345\n" +
                "                            </td>\n" +
                "                            \n" +
                "                            <td>\n" +
                "                                 \n" +
                currentUserAPI.getUserId() + "<br>\n" +
                currentUserAPI.getUserName() + "\n" +
                "                            \n" +
                "\n" +
                "\n" +
                "                               \n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            \n" +
                "            \n" +
                "            <tr class=\"heading\">\n" +
                "                <td>\n" +
                "                    Item\n" +
                "                </td>\n" +
                "                <td>\n" +
                "                    Quantity\n" +
                "                </td>\n" +
                "                <td>\n" +
                "                    Price\n" +
                "                </td>\n" +
                "            </tr>");

        if (!invoices.isEmpty()) {
            for (Invoice invoice : invoices) {
                htmlContent.append(" <tr class=\"item\">\n" +
                        "                <td>\n" +
                        invoice.getProductName() + "\n" +
                        "                </td>\n" +
                        "\n" +
                        "                <td>\n" +
                        invoice.getQuantity() + "\n" +
                        "                </td>\n" +
                        "                \n" +
                        "                <td>\n" +
                        invoice.getPrice() + "\n" +
                        "                </td>\n" +
                        "            </tr>");
            }

            if (!invoices.isEmpty()) {
                int total = 0;
                for (Invoice invoice : invoices) {
                    total += invoice.getPrice();
                }
                htmlContent.append("<tr class=\"total\">\n" +
                        "                <td></td>\n" +
                        "                \n" +
                        "                <td>\n" +
                        "                   Total: Rs." + total + "\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>");
            }
        }
        return htmlContent.toString();
    }
}

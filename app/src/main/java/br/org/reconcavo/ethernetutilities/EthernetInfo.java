package br.org.reconcavo.ethernetutilities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

public class EthernetInfo extends AppCompatActivity {
    private EditText ip_field;
    private EditText netmask_field;
    private EditText gateway_field;
    private Switch updown_switch;
    private CheckBox use_dhcp_box;
    TextView eth_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethernet_info);

        loadElements();
    }

    private void loadElements(){
        eth_status = (TextView) findViewById(R.id.eth_interface_status);
        ip_field = (EditText) findViewById(R.id.edt_ip);
        netmask_field = (EditText) findViewById(R.id.edt_netmask);
        gateway_field = (EditText) findViewById(R.id.edt_gateway);
        use_dhcp_box = (CheckBox) findViewById(R.id.check_use_DHCP);

        updown_switch = (Switch) findViewById(R.id.switch_eth_up_down);
        try {
            updown_switch.setChecked(EthernetRuntimeExec.isEthOn());
        } catch (IOException e) {
            eth_status.setTextColor(Color.RED);
            eth_status.setText("COULD NOT GET ETH0 STATUS");
            updown_switch.setEnabled(false);
            e.printStackTrace();
        }
        updown_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    eth_status.setTextColor(Color.GREEN);
                    eth_status.setText("ETHERNET UP");
                    EthernetRuntimeExec.turnEthOn(true);
                }else{
                    eth_status.setTextColor(Color.RED);
                    eth_status.setText("ETHERNET DOWN");
                    EthernetRuntimeExec.turnEthOn(false);
                }
            }
        });
    }

    public void doConnect(View view){
        if(use_dhcp_box.isChecked()){
            EthernetRuntimeExec.connect();
        }else{
            EthernetRuntimeExec.connect(loadConnectionInfo());
        }
    }

    private ConnectionInfo loadConnectionInfo(){
        ConnectionInfo info = new ConnectionInfo();
        info.setIp(ip_field.getText().toString());
        info.setNetmask(netmask_field.getText().toString());
        info.setGateway(gateway_field.getText().toString());

        return info;
    }
}

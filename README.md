# super_metrics_misc

## Product Details

## MQTT

### Web Client Messages

* Cart Location
 * Chanel: monitor/cartId/location
 * Example: {"px":0.16085714285714287,"py":0.12}
* Change BLE threshold
 * Channel: monitor/cartId/command
 * Example: {"changeThreshold":true, "mac":"34:b1:f7:d3:91:f8", "threshold":-50}

### Cart Detector Messages

* Revolution
 * Chanel: cart/cartId/revolution
 * Example: {"start_time": 1488995981.953236, "forward_counter": 371, "backward_counter": 0, "forward_revolution": true}  
* BLE
 * Chanel: cart/cartId/ble
 * Example: cart/cartId/ble {"nearest_rssi": -36, "start_time": 1488995907, "mac": "34:b1:f7:d3:9e:2b", "nearest_time": 1488995909, "end_time": 1488995916}

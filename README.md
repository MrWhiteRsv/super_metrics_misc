# super_metrics_misc

## Product Details

Example: {"category": "asian", "location_str": "aisle 6", "uuid": "8084360115101", "ingredients": "100% MECHANICALLY (EXPELLER) PRESSED REFINED SESAME OIL", "ndbno": "45017750", "nutrients": [{"unit": "kcal", "name": "Energy", "value": "857"}, {"unit": "mg", "name": "Cholesterol", "value": "0"}], "price": "$7.99", "location_px": {"px": 0.215, "py": 0.58}, "description": "SPECTRUM, SESAME OIL, UPC: 8084360115101", "discount_percent": 0, "images": ["sesame_oil.png"], "name": "Sesame Oil"}

## MQTT

### Web Client Messages

* Cart Location
  * Chanel: monitor/cartId/location
  * Example: {"px":0.16085714285714287,"py":0.12}

#### Commands

* Change BLE threshold
  * Channel: monitor/cartId/command
  * Example: {"changeThreshold":true, "mac":"34:b1:f7:d3:91:f8", "threshold":-50}

* Capture Image
  * Channel: monitor/cartId/command
  * Example: {"changeThreshold":true, "mac":"34:b1:f7:d3:91:f8", "threshold":-50}

### Cart Detector Messages

* Revolution
 * Chanel: cart/cartId/revolution
 * Example: {"start_time": 1488995981.953236, "forward_counter": 371, "backward_counter": 0, "forward_revolution": true}  
* BLE
 * Chanel: cart/cartId/ble
 * Example: cart/cartId/ble {"nearest_rssi": -36, "start_time": 1488995907, "mac": "34:b1:f7:d3:9e:2b", "nearest_time": 1488995909, "end_time": 1488995916}

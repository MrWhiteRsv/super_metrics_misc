import sys, getopt

import calendar
import json
import time

def create_ble_event(nearest_rssi, mac, start_time, nearest_time, end_time):
  # {"nearest_rssi": -61, "start_time": 1480191915.954596, "topic": "cart/cartId/ble",
  # "mac": "34:b1:f7:d3:9d:a3", "end_time": 1480191919.968618, "nearest_time": 1480191915.954596}
  event = {}
  event["topic"] = "cart/cartId/ble"
  event["nearest_rssi"] = nearest_rssi
  event["mac"] = mac
  event["start_time"] = start_time
  event["nearest_time"] = nearest_time
  event["end_time"] = end_time    
  return event

def create_revolution_event(start_time, forward_counter, backward_counter, forward_revolution):
  # {"topic": "cart/cartId/revolution", "start_time": 1487276190.920604, "forward_counter": 6, "backward_counter": 15}
  event = {}
  event["topic"] = "cart/cartId/revolution"
  event["start_time"] = start_time
  event["forward_counter"] = forward_counter
  event["backward_counter"] = backward_counter
  event["forward_revolution"] = forward_revolution
  return event

def write_fake_segment(num_rev, forward, outfile, state):
  """
    Write num_rev either forward or backward to outfile and update counters.
    Args:
      num_rev (int): number of revolutions in segment.
      forward (boolean): true if moving forward, flase if backward.
      outfile: the file in which to write the segment revolution events as json.
      state (dictioary): holding both 'forward_counter' and
          'backward_counter' value along with 'time_since_epoch'.
    """
  for i in range(num_rev):
    state['forward_counter'] += 1 if forward else 0
    state['backward_counter'] += 0 if forward else 1
    state['time_since_epoch'] = state['time_since_epoch'] + 80.0
    outfile.write(json.dumps(create_revolution_event(start_time = state['time_since_epoch'],
        forward_counter = state['forward_counter'],
        backward_counter = state['backward_counter'],
        forward_revolution = forward)) +'\n')

def make_fake_trip(outfile_name):
  state = {}
  state['forward_counter'] = 0
  state['backward_counter'] = 0
  state['time_since_epoch'] = calendar.timegm(time.gmtime())
  
  outfile = open(outfile_name, 'w+')
  revolution_counter = 0
  time_since_epoch = calendar.timegm(time.gmtime())
  
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:91:c8',
      start_time = time_since_epoch - 15, nearest_time = time_since_epoch,
      end_time = time_since_epoch + 15)
  outfile.write(json.dumps(ble_event) +'\n')
  
  write_fake_segment(num_rev = 10, forward = True, outfile = outfile, state = state)
  time_since_epoch = time_since_epoch + 100.0
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:9c:cb',
      start_time = time_since_epoch - 15, nearest_time = time_since_epoch,
      end_time = time_since_epoch + 15)
  outfile.write(json.dumps(ble_event) + '\n')  
  
  write_fake_segment(num_rev = 30, forward = True, outfile = outfile, state = state)
  write_fake_segment(num_rev = 20, forward = False, outfile = outfile, state = state)
  write_fake_segment(num_rev = 70, forward = True, outfile = outfile, state = state)
  time_since_epoch = time_since_epoch + 100.0
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:91:e4',
      start_time = time_since_epoch - 15, nearest_time = time_since_epoch,
      end_time = time_since_epoch + 15)
  outfile.write(json.dumps(ble_event) + '\n') 
    
  write_fake_segment(num_rev = 10, forward = True, outfile = outfile, state = state)
  time_since_epoch = time_since_epoch + 100.0
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:9d:eb',
      start_time = time_since_epoch - 15, nearest_time = time_since_epoch,
      end_time = time_since_epoch + 15)
  outfile.write(json.dumps(ble_event) + '\n') 
  
  write_fake_segment(num_rev = 80, forward = True, outfile = outfile, state = state)
  time_since_epoch = time_since_epoch + 100.0
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:91:c8',
      start_time = time_since_epoch - 15, nearest_time = time_since_epoch,
      end_time = time_since_epoch + 15)
  outfile.write(json.dumps(ble_event) + '\n')   
  outfile.close()
  
def main(argv):
  try:
    opts, args = getopt.getopt(argv, "ho:", ['outfile='])
  except getopt.GetoptError:
    print 'fake_trip_generator.py -o <res_file>'
    sys.exit(2)
  for opt, arg in opts:
    if opt == '-h':
      print 'python fake_trip_generator.py -o <res_file>'
      sys.exit()
    if opt in ("-o", "--outfile"):
      outfile = arg
  make_fake_trip(outfile)
  
if __name__ == '__main__':
    main(sys.argv[1:])

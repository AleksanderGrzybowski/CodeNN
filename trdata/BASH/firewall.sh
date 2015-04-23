#! /bin/sh

echo "Iptables reset"
i=iptables
$i -F
$i -X
$i -F -t nat
$i -X -t nat

echo 1 > /proc/sys/net/ipv4/ip_forward # dla vbox konieczne
echo 1 > /proc/sys/net/ipv4/icmp_echo_ignore_all # nie wiem czy wszystko będzie działać

# domyślne reguły
$i -P OUTPUT ACCEPT
$i -P INPUT DROP
$i -P FORWARD DROP

# lo jest dozwolone
$i -A INPUT -i lo -j ACCEPT
$i -A FORWARD -o lo -j ACCEPT
$i -A FORWARD -i lo -j ACCEPT

# domyślne
$i -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

# ssh dla galery
$i -A INPUT -p tcp --dport 22 -j ACCEPT

# wszystko może iść do vm
$i -A FORWARD -i vboxnet0 -j ACCEPT
$i -A FORWARD -o vboxnet0 -j ACCEPT
$i -A INPUT -i vboxnet0 -j ACCEPT

# to jest, ponieważ na pentagramie nie wiem jak zezwolić
# na akceptowanie ruchu z 192.168.7.0/24 do internetu
# na edimaksie działało normalnie...
$i -t nat -A POSTROUTING -j MASQUERADE

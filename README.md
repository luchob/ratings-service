# ratings-service
A small base for potential talk @ Java2Days 2019

##How to run your service locally in Kubernetes.

1.  [ Install and configure ](#minikube-install)
2.  [ Build docker image ](#backend-image) 
3.  [ Deploy mysql ](#deploy-mysql) 
    - [ Configure with ConfigMaps ](#mysql-configure) 
    - [ Create secret to save the password ](#mysql-configure-secret) 
    - [ Where is the data: PV and PVC ](#mysql-data) 
    - [ Deploy mysql ](#mysql-deploy) 
    - [ Create a service to make mysql visible ](#mysql-service) 
4.  [ Deploy your service ](#rating-backend)
    - [ Deploy your backend service ](#rating-backend)
    - [ Create a service to make the service visible ](#rating-service) 
5.  [ Scale ](#scale) 
6.  [ Undo deployment ](#undo) 
7.  [ Stop ](#stop) 
8.  [ Delete ](#delete) 

<a name="minikube-install"></a>
###  Minikube install and configure

Usually for this purpose is used Minikube.
Minikube is a tool that makes it easy to run Kubernetes locally. 
    

[Install Minikube] (https://kubernetes.io/docs/tasks/tools/install-minikube/)
    
Minikube runs a single-node Kubernetes cluster inside a Virtual Machine (VM).

Also will install `kubectl` which is client which will communicate with API server running on cluster master node.


- Start Minikube and create a cluster:
```
minikube start
```

- Reuse the docker daemon locally:
   
   `eval $(minikube docker-env)`
   
   Configure `kubectl` to use the context minikube:
   ``kubectl config use-context minikube``
   
   A context is a group of access parameters: cluster, a user, and a namespace.



<a name="backend-image"></a>
2. Build a docker image of the backend service:

   `docker build -t ratings-service -f deployment/Dockerfile .`



<a name="deploy-mysql"></a>
###  Mysql configure and deploy.

In order to connect to mysql we need configurations like: host, port, user, pass

Kubernetes object ConfigMaps allow you to decouple configuration artifacts from image content to keep containerized applications portable. 

<a name="mysql-configure"></a>
**Example ConfigMaps for our mysql in file minikube-config.yaml:**
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: ratings-config
data:
  mysql.host: "mysql-service"
  mysql.port: "3306"
  mysql.user: "root"
```
Apply the configuration in minikube
``kubectl apply -f minikube-config.yaml``

<a name="mysql-configure-secret"></a>
Kubernetes secret objects let you store and manage sensitive information, such as passwords.

Create the file `minikube-credentials.yaml`:
```
apiVersion: v1
kind: Secret
metadata:
  name: mysql-credentials
type: Opaque
data:
  # base 64 encoded
  password: dG9wc2VjcmV0
```
Apply the password configuration:
``kubectl apply -f minikube-credentials.yaml``

<a name="mysql-data"></a>
- Where is our Data?

**A PersistentVolume (PV)** is a piece of storage in the cluster that has been provisioned by an administrator or dynamically provisioned using Storage Classes.

minikube-mysql-pv.yaml
```
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mysql-pv-volume
spec:
  accessModes:
    - ReadWriteOnce
  capacity:
    storage: 1Gi
  hostPath:
    path: /tmp/data
  persistentVolumeReclaimPolicy: Retain
  storageClassName: manual
  volumeMode: Filesystem
```

Apply the PV:
`kubectl apply -f minikube-mysql-pv.yaml`

**A PersistentVolumeClaim (PVC)** is a request for storage by a user. It is similar to a Pod. Pods consume node resources and PVCs consume PV resources.

minikube-mysql-pvc.yaml
```
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pv-claim
spec:
  storageClassName: manual
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi

```
  
Apply the PVC:
`kubectl apply -f minikube-mysql-pvc.yaml`

<a name="mysql-service"></a>
Create a service 
```
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
spec:
  ports:
    - port: 3306
  selector:
    app: mysql
  clusterIP: None
```
Apply service:
`kubectl apply -f minikube-mysql-service.yaml`


<a name="mysql-deploy"></a>
### Service deployment:
```
spec:
  selector:
    matchLabels:
      app: mysql
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
        - image: mysql:5.6
          name: mysql
          env:
            - name: MYSQL_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-credentials
                  key: password
          ports:
            - containerPort: 3306
              name: mysql
          volumeMounts:
            - name: mysql-persistent-storage
              mountPath: /var/lib/mysql
      volumes:
        - name: mysql-persistent-storage
          persistentVolumeClaim:
            claimName: mysql-pv-claim
```

<a name="rating-backend"></a>
Deploy ratings-service:
`kubectl apply -f minikube-mysql-deployment.yaml`


```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ratings-backend
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ratings-backend
  template:
    metadata:
      labels:
        app: ratings-backend
      annotations:
        balev.eu/author: "Lachezar Balev"
    spec:
      containers:
        - name: ratings-backend
          image: luchob/ratings-service:latest
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
              name: server-port
          env:
            - name: MYSQL_HOST
              valueFrom:
                configMapKeyRef:
                  name: ratings-config
                  key: mysql.host
            - name: MYSQL_PORT
              valueFrom:
                configMapKeyRef:
                  name: ratings-config
                  key: mysql.port
            - name: MYSQL_USER
              valueFrom:
                configMapKeyRef:
                  name: ratings-config
                  key: mysql.user
            - name: MYSQL_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-credentials
                  key: password
```

`kubectl apply -f minikube-backend.yaml`

<a name="rating-service"></a>
`kubectl apply -f minikube-backend-service.yaml`

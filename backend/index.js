import  express  from "express";
import mongoose from "mongoose"
import router from "./routes/user-routes";
import postRouter from "./routes/post-routes";

const app = express();
app.use(express.json())
app.use("/api/user",router)
app.use("/api/post",postRouter)

mongoose.connect(
    "mongodb+srv://admin:Mumbai123@track-mate-cluster.u3sjqum.mongodb.net/Track-Mate?retryWrites=true&w=majority"
).then(()=>app.listen(5000 , '127.0.0.1')).then(()=>{
    console.log("Database Connected")
}).catch((err)=>{
    console.log(err)
});

app.use("/" , (req,res,next)=>{
    res.send("Hello World");
});

app.listen(process.env.PORT || 5000 , ()=>{
    console.log("Server Connected")
});

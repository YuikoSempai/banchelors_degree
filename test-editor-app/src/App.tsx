import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import CreateTest from "./pages/CreateTest";
import EditTest from "./pages/EditTest";
import Sidebar from "./components/Sidebar";
import AllTestsPage from "./pages/AllTestsPage";
import RoomsPage from "./pages/RoomsPage";
import RoomResultsPage from "./pages/RoomResultsPage";

export default function App() {
  return (
    <div className="flex">
      <Sidebar />
      <main className="flex-1 p-6">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/create/:id" element={<CreateTest />} />
          {/* <Route path="/edit/:id" element={<EditTest />} /> */}
          <Route path="/tests" element={<AllTestsPage />} />
          <Route path="/rooms" element={<RoomsPage />} />
          <Route path="/rooms/result" element={<RoomResultsPage rooms={[{id: 1, name: 'test'}]}/>} />
        </Routes>
      </main>
    </div>
  );
}

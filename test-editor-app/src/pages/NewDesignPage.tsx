// import { Card, CardContent } from "@/components/ui/card";
// import { Button } from "@/components/ui/button";
// import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
// import { PlusCircle, BarChart2, FileText, Users } from "lucide-react";

// export default function TeacherDashboard() {
//   return (
//     <div className="p-6 space-y-6">
//       <h1 className="text-3xl font-bold">Панель преподавателя</h1>

//       <Tabs defaultValue="tests" className="w-full">
//         <TabsList className="grid grid-cols-4 gap-2 bg-muted p-1 rounded-xl">
//           <TabsTrigger value="tests"><FileText className="inline mr-2" />Тесты</TabsTrigger>
//           <TabsTrigger value="results"><BarChart2 className="inline mr-2" />Результаты</TabsTrigger>
//           <TabsTrigger value="groups"><Users className="inline mr-2" />Группы</TabsTrigger>
//           <TabsTrigger value="create"><PlusCircle className="inline mr-2" />Создать тест</TabsTrigger>
//         </TabsList>

//         <TabsContent value="tests">
//           <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
//             <Card>
//               <CardContent className="p-4">
//                 <h2 className="text-xl font-semibold">Тест по математике</h2>
//                 <p className="text-muted-foreground">30 вопросов • 60 мин</p>
//                 <Button className="mt-4 w-full">Редактировать</Button>
//               </CardContent>
//             </Card>
//             {/* Примеры дополнительных тестов */}
//           </div>
//         </TabsContent>

//         <TabsContent value="results">
//           <div className="mt-4">
//             <h2 className="text-xl font-semibold mb-4">Аналитика по тестам</h2>
//             {/* Здесь можно вставить графики и таблицы */}
//             <p className="text-muted-foreground">Графики с результатами появятся здесь.</p>
//           </div>
//         </TabsContent>

//         <TabsContent value="groups">
//           <div className="mt-4">
//             <h2 className="text-xl font-semibold mb-4">Мои группы</h2>
//             {/* Здесь отображаются учебные группы */}
//             <p className="text-muted-foreground">Пока нет созданных групп.</p>
//             <Button className="mt-4">Создать группу</Button>
//           </div>
//         </TabsContent>

//         <TabsContent value="create">
//           <div className="mt-4">
//             <h2 className="text-xl font-semibold mb-4">Создание нового теста</h2>
//             <Button>Начать</Button>
//           </div>
//         </TabsContent>
//       </Tabs>
//     </div>
//   );
// }

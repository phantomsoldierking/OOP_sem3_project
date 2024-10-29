"use client"

import { useState, useEffect } from "react"
import Papa from "papaparse"
import { Calendar, Clock, Upload, Sun, Moon, Book, Coffee, Utensils, Pencil, Music, Code, Dumbbell } from "lucide-react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Switch } from "@/components/ui/switch"
import { cn } from "@/lib/utils"

const subjectColors = {
  "Math": "bg-red-400 text-white",
  "Science": "bg-green-400 text-white",
  "English": "bg-blue-400 text-white",
  "History": "bg-yellow-400 text-white",
  "Art": "bg-purple-400 text-white",
  "Music": "bg-pink-400 text-white",
  "PE": "bg-orange-400 text-white",
  "Computer Science": "bg-indigo-400 text-white",
  "Break": "bg-gray-400 text-white",
  "Lunch": "bg-amber-400 text-white",
}

const subjectIcons = {
  "Math": <Pencil size={16} />,
  "Science": <Book size={16} />,
  "English": <Book size={16} />,
  "History": <Book size={16} />,
  "Art": <Pencil size={16} />,
  "Music": <Music size={16} />,
  "PE": <Dumbbell size={16} />,
  "Computer Science": <Code size={16} />,
  "Break": <Coffee size={16} />,
  "Lunch": <Utensils size={16} />,
}

export default function TimetableViewer() {
  const [timetableData, setTimetableData] = useState<string[][]>([])
  const [isDarkMode, setIsDarkMode] = useState(false)
  const [isLoaded, setIsLoaded] = useState(false)

  useEffect(() => {
    setIsLoaded(true)
  }, [])

  const handleFileUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    if (file) {
      Papa.parse(file, {
        complete: (result) => {
          setTimetableData(result.data as string[][])
        },
      })
    }
  }

  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode)
  }

  const getSubjectColor = (subject: string) => {
    const key = Object.keys(subjectColors).find(k => subject.includes(k))
    return key ? subjectColors[key as keyof typeof subjectColors] : "bg-gray-400 text-white"
  }

  const getSubjectIcon = (subject: string) => {
    const key = Object.keys(subjectIcons).find(k => subject.includes(k))
    return key ? subjectIcons[key as keyof typeof subjectIcons] : <Clock size={16} />
  }

  return (
    <div className={cn(
      "min-h-screen p-8 transition-colors duration-300 ease-in-out bg-gradient-to-br",
      isDarkMode
        ? "from-gray-900 via-purple-900 to-violet-900"
        : "from-blue-200 via-purple-200 to-pink-200"
    )}>
      <Card className={cn(
        "max-w-4xl mx-auto overflow-hidden transition-all duration-300 ease-in-out",
        isLoaded ? "opacity-100 translate-y-0" : "opacity-0 translate-y-4",
        isDarkMode ? "bg-gray-800 text-white" : "bg-white",
        "border-4 border-purple-500 shadow-2xl"
      )}>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2 bg-gradient-to-r from-purple-500 to-pink-500 text-white">
          <CardTitle className="text-3xl font-bold">
            Timetable Viewer
          </CardTitle>
          <div className="flex items-center space-x-2">
            <Sun className="h-5 w-5 text-yellow-300" />
            <Switch
              id="dark-mode"
              checked={isDarkMode}
              onCheckedChange={toggleDarkMode}
              className="data-[state=checked]:bg-purple-700"
            />
            <Moon className="h-5 w-5 text-purple-300" />
          </div>
        </CardHeader>
        <CardContent className="p-6">
          <div className="flex flex-col items-center space-y-6">
            <label htmlFor="csv-upload" className="w-full">
              <div className={cn(
                "flex items-center justify-center w-full h-40 px-4 transition-all duration-300 ease-in-out border-4 border-dashed rounded-md appearance-none cursor-pointer",
                isDarkMode
                  ? "border-purple-500 hover:border-pink-500 bg-gray-800"
                  : "border-purple-300 hover:border-pink-400 bg-purple-100"
              )}>
                <span className="flex flex-col items-center space-y-2">
                  <Upload size={40} className="text-purple-500" />
                  <span className={cn(
                    "font-medium text-lg",
                    isDarkMode ? "text-purple-300" : "text-purple-700"
                  )}>
                    Drop your CSV file here, or click to select
                  </span>
                </span>
              </div>
              <input
                id="csv-upload"
                type="file"
                accept=".csv"
                className="hidden"
                onChange={handleFileUpload}
              />
            </label>
            {timetableData.length > 0 && (
              <div className="w-full overflow-x-auto rounded-lg shadow-lg">
                <Table>
                  <TableHeader>
                    <TableRow className="bg-gradient-to-r from-purple-600 to-pink-600 text-white">
                      {timetableData[0].map((header, index) => (
                        <TableHead key={index} className="text-center py-4 text-lg font-bold">
                          {index === 0 ? (
                            <span className="flex items-center justify-center">
                              <Calendar className="mr-2" size={20} />
                              {header}
                            </span>
                          ) : (
                            <span className="flex items-center justify-center">
                              <Clock className="mr-2" size={20} />
                              {header}
                            </span>
                          )}
                        </TableHead>
                      ))}
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {timetableData.slice(1).map((row, rowIndex) => (
                      <TableRow
                        key={rowIndex}
                        className={cn(
                          "transition-colors duration-200 ease-in-out",
                          rowIndex % 2 === 0
                            ? isDarkMode ? "bg-gray-800" : "bg-purple-50"
                            : isDarkMode ? "bg-gray-700" : "bg-white",
                          isDarkMode
                            ? "hover:bg-purple-900"
                            : "hover:bg-purple-100"
                        )}
                      >
                        {row.map((cell, cellIndex) => (
                          <TableCell key={cellIndex} className="text-center py-4">
                            {cellIndex === 0 ? (
                              <span className="font-semibold text-lg">{cell}</span>
                            ) : (
                              <span className={cn(
                                "inline-flex items-center px-3 py-2 rounded-full text-sm font-medium shadow-md",
                                getSubjectColor(cell)
                              )}>
                                {getSubjectIcon(cell)}
                                <span className="ml-2">{cell}</span>
                              </span>
                            )}
                          </TableCell>
                        ))}
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
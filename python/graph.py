from mpl_toolkits.mplot3d import Axes3D
from matplotlib import pyplot as plt
import numpy as np
import pandas as pd
# x = np.arange(-500, 500, 1)
# y = np.arange(-500, 500, 1)
# X, Y = np.meshgrid(x, y)
# Z = -X * np.sin(np.sqrt(np.abs(X))) -Y * np.sin(np.sqrt(np.abs(Y))) + 418.9829 * 2
#
# fig = plt.figure()
# ax = Axes3D(fig)
# ax.set_zlabel('z')
# plt.title('Axes3D')
# plt.xlabel('x')
# plt.ylabel('y')
# ax.plot_surface(X, Y, Z, rstride=1, cstride=1, cmap='rainbow')
# plt.savefig(r'Axes3D.png', dpi=1500)
# plt.show()
#
# plt.figure('Contourf')
# plt.xlabel('x')
# plt.ylabel('y')
# plt.contourf(X, Y, Z, 8, cmap='jet')
# plt.savefig(r'Contourf.png', dpi=1500)
# plt.show()

df = pd.read_csv(r'../result.csv', sep=',', index_col=False)
omegas = np.array(df['omega'].unique())
omegas.sort()
ccs = np.array(df['cc'].unique())
ccs.sort()

avg = np.array([
    [
        (df[(df['omega'] == omega) & (df['cc'] == cc)])['avg'].values[0]
        for omega in omegas
    ]
    for cc in ccs
])
fig = plt.figure()
plt.xlabel('pc')
plt.ylabel('pm')
plt.contourf(omegas, ccs, avg, 20, cmap='jet')
plt.savefig(r'ParameterInvestigation.png', dpi=1500)
plt.show()
df = pd.DataFrame(avg)
df.columns = omegas
df.index = ccs
df.to_csv(r'ParameterInvestigation.csv')
